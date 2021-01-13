package com.agtinternational.iotcrawler.core.clients;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.google.gson.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


//ToDo: timeout for requests

public class IoTCrawlerRPCClient extends IoTCrawlerRESTClient implements AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);

    //Semaphore callFinishedMutex;



    //NgsiLDClient ngsiLDClient;


    RabbitClient rabbitClient;
    //RabbitMQRpcClient rabbitRpcClient;

    public IoTCrawlerRPCClient(String ngsild_endpoint_url, String graphQLEndpoint, String rabbitHost){
        super(ngsild_endpoint_url, graphQLEndpoint);
        rabbitClient = new RabbitClient(rabbitHost);
    }

    @Override
    public void init() throws Exception {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rabbitClient.init();
                } catch (Exception e) {
                    LOGGER.error("Failed to init rabbit client");
                    //e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public String subscribeToStream(String streamId, Function<StreamObservation, Void> onChange) throws Exception {
        String subscriptionId = super.subscribeToStream(streamId, onChange);
        return subscriptionId;
    }



    @Override
    public String subscribe(Subscription subscription, Function<byte[], Void> onChange) throws Exception {
        String subscriptionId = super.subscribe(subscription, onChange);
        rabbitClient.initRabbitMQListener(subscriptionId, new Function<byte[], Void>() {
            @Override
            public Void apply(byte[] bytes) {
                onChange.apply(bytes);
                return null;
            }
        });
        return subscriptionId;
    }



    private JsonElement parseResponse(String response) throws Exception{

        JsonElement result = new JsonObject();
        try {
            result = parser.parse(response);
        }
        catch (Exception e){
            Exception exception = new Exception("Failed to parse an RPC response:" +e.getLocalizedMessage(), e.getCause());
            LOGGER.error(exception.getLocalizedMessage());
            exception.printStackTrace();
            throw exception;
        }

        if(result instanceof JsonObject && ((JsonObject)result).has("error")) {
            Object error = ((JsonObject)result).get("error");
            String message = (error instanceof JsonObject? ((JsonObject)error).toString(): String.valueOf(error));
            Exception exception = new Exception(message);
//            LOGGER.error(exception.getLocalizedMessage());
//            exception.printStackTrace();
            throw exception;
        }

        return result;
    }

    @Override
    public void close(){
       rabbitClient.close();
    }
}
