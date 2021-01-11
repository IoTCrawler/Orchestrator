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

import com.agtinternational.iotcrawler.core.ontologies.SOSA;
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

        //parser = new JsonParser();
//        String queueName=null;
//
//        attempt=0;
//        while(rabbitRpcClient==null && attempt<2) {
//
//            channel = connection.createChannel();
//            queueName = channel.queueDeclare().getQueue();
//
//            try {
//                if(attempt>0) {
//                    LOGGER.debug("Trying to create exchange {}", IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
//                    channel.exchangeDeclare(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, "fanout", false, true, null);
//                }
//
//                LOGGER.debug("Trying to bind the queue {} to exchange {}", queueName, IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
//                channel.queueBind(queueName, IOTCRAWLER_NOTIFICATIONS_EXCHANGE, "");
//                LOGGER.debug("Initing RabbitRpcClient");
//                rabbitRpcClient = RabbitMQRpcClient.create(connection, IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
//                rabbitRpcClient.setMaxWaitingTime(30000);
//            } catch (IOException e) {
//
//            } catch (Exception e) {
//                LOGGER.debug("Failed to bind to queue, will create it and try again");
//                e.printStackTrace();
//                break;
//            }
//            attempt++;
//
//        }

//        try {
//            LOGGER.debug("Initing queue consumer");
//            Consumer consumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    try {
//                        handleCmd(body, properties.getReplyTo());
//                    } catch (Exception e) {
//                        LOGGER.error("Exception while trying to handle incoming command.", e);
//                    }
//                }
//            };
//
//            channel.basicConsume(queueName, true, consumer);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            LOGGER.error("Failed to init consumer");
//        }

        //callFinishedMutex = new Semaphore(0);
    }



    @Override
    public String subscribeTo(String streamId, Function<StreamObservation, Void> onChange) throws Exception {
        String subscriptionId = super.subscribeTo(streamId, onChange);

        //String queueName = rabbitClient.declareBoundQueue(subscriptionId);
        rabbitClient.initRabbitMQListener(subscriptionId, new Function<byte[], Void>() {
            @Override
            public Void apply(byte[] bytes) {
                StreamObservation streamObservation = null;
                try {
                    String jsonString = new String(bytes);
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonString);
                    JsonArray data = (JsonArray) jsonObject.get("data");
                    for(JsonElement item : data) {
                        //JsonObject jsonObject1 = (JsonObject)item;
                        EntityLD entityLD = EntityLD.fromJsonString(item.toString());
                        streamObservation = StreamObservation.fromEntity(entityLD);
                        //EntityLD entityLD = StreamObservation.fromJsonObject(jsonObject1.toString());
//                        streamObservation = new StreamObservation(jsonObject1.get("id").getAsString());
//                        if(jsonObject1.has(SOSA.hasSimpleResult)) {
//                            JsonObject result = (JsonObject) jsonObject1.get(SOSA.hasSimpleResult);
//                            streamObservation.hasSimpleResult(result.get("value"));
//                        }
//                        if(jsonObject1.has(SOSA.resultTime))
//                            streamObservation.resultTime(jsonObject1.get(SOSA.resultTime));
                    }
                }
                catch (Exception e){
                    LOGGER.error("Failed to create StreamObservation from Json: {}", e.getLocalizedMessage());
                    return null;
                }
                onChange.apply(streamObservation);
                return null;
            }
        });
        return subscriptionId;
    }



    @Override
    public void subscribeTo(Subscription subscription, Function<byte[], Void> onChange) throws Exception {
        super.subscribeTo(subscription, onChange);
        String abc = "";
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
