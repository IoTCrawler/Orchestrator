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

import com.agtinternational.iotcrawler.core.RabbitMQRpcClient;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.commands.*;
import com.agtinternational.iotcrawler.core.interfaces.IoTCrawlerClient;
import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.rabbitmq.client.*;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_NOTIFICATIONS_EXCHANGE;


//ToDo: timeout for requests

public class IoTCrawlerRPCClient extends IoTCrawlerRESTClient implements AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);

    //Semaphore callFinishedMutex;

    Connection connection;
    Channel channel;
    JsonParser parser;

    //NgsiLDClient ngsiLDClient;

    String rabbitHost;
    RabbitMQRpcClient rabbitRpcClient;


    public IoTCrawlerRPCClient(String ngsild_endpoint_url, String graphQLEndpoint, String rabbitHost){
        super(ngsild_endpoint_url, graphQLEndpoint);
        this.rabbitHost = rabbitHost;
    }

    @Override
    public void init() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        String[] splitted = rabbitHost.split(":");
        factory.setHost(splitted[0]);
        if(splitted.length>1)
            factory.setPort(Integer.parseInt(splitted[1]));

        parser = new JsonParser();

        int attempt = 0;
        while (connection==null && attempt<12){
            LOGGER.debug("Trying connect to Rabbit at {}", rabbitHost);
            attempt++;
            try {
                connection = factory.newConnection();
            } catch (Exception e) {
                LOGGER.error("Failed to init rabbit connection. Trying another attempt ({} or {})", attempt, 12);
                e.printStackTrace();
            }
            if(connection==null)
                Thread.sleep(5000);
        }

        if(connection==null)
            throw new Exception("No connection to Rabbit established in "+attempt+" attempts");


        String queueName=null;

        attempt=0;
        while(rabbitRpcClient==null && attempt<2) {

            channel = connection.createChannel();
            queueName = channel.queueDeclare().getQueue();

            try {
                if(attempt>0) {
                    LOGGER.debug("Trying to create exchange {}", IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
                    channel.exchangeDeclare(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, "fanout", false, true, null);
                }

                LOGGER.debug("Trying to bind the queue {} to exchange {}", queueName, IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
                channel.queueBind(queueName, IOTCRAWLER_NOTIFICATIONS_EXCHANGE, "");
                LOGGER.debug("Initing RabbitRpcClient");
                rabbitRpcClient = RabbitMQRpcClient.create(connection, IOTCRAWLER_NOTIFICATIONS_EXCHANGE);
                rabbitRpcClient.setMaxWaitingTime(30000);
            } catch (IOException e) {

            } catch (Exception e) {
                LOGGER.debug("Failed to bind to queue, will create it and try again");
                e.printStackTrace();
                break;
            }
            attempt++;

        }

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
    public String subscribeTo(String streamId, String endpointUrl, Function<StreamObservation, Void> onChange) throws Exception {
        String subscriptionId = super.subscribeTo(streamId, endpointUrl, onChange);
        initRabbitMQListener(subscriptionId, onChange);
        return subscriptionId;
    }

    @Override
    public String subscribeTo(Subscription subscription, Function<StreamObservation, Void> onChange) throws Exception {
        String subscriptionId = super.subscribeTo(subscription, onChange);
        initRabbitMQListener(subscriptionId, onChange);
        return subscriptionId;
    }

    private void initRabbitMQListener(String subscriptionId, Function<StreamObservation, Void> onChange){
        try {
            LOGGER.debug("Initing {} queue listener");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        LOGGER.debug("Notification received");
                        onChange.apply(null);
                        //handleCmd(body, properties.getReplyTo());
                    } catch (Exception e) {
                        LOGGER.error("Exception while trying to handle incoming command.", e);
                    }
                }
            };

            channel.basicConsume(subscriptionId, true, consumer);
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Failed to init consumer");
        }
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
        try {
            connection.close();
        }
        catch (Exception e){
            LOGGER.error("Failed to close: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
