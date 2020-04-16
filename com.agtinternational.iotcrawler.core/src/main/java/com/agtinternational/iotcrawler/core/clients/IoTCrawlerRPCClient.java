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
import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
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

public class IoTCrawlerRPCClient extends IotCrawlerClient implements AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);

    //Semaphore callFinishedMutex;

    Connection connection;
    Channel channel;
    JsonParser parser;

    NgsiLDClient ngsiLDClient;

    String rabbitHost;
    RabbitMQRpcClient rabbitRpcClient;
    Consumer consumer;

    public IoTCrawlerRPCClient(String rabbitHost){
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

        try {
            LOGGER.debug("Initing queue consumer");
            consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        handleCmd(body, properties.getReplyTo());
                    } catch (Exception e) {
                        LOGGER.error("Exception while trying to handle incoming command.", e);
                    }
                }
            };

            channel.basicConsume(queueName, true, consumer);
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Failed to init consumer");
        }

        //callFinishedMutex = new Semaphore(0);
    }

    protected void handleCmd(byte bytes[], String replyTo) {

    }

    @Override
    public void run() throws Exception {

    }




    @Override
    public List<IoTStream> getStreams(Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        List<IoTStream> ret = new ArrayList<>();
        throw new NotImplementedException("");
//        GetEntitiesCommand command = new GetEntitiesCommand(IoTStream.getTypeUri(), query, ranking, offset, limit);
//        List<EntityLD> entities = execute(command);
//
//        for(EntityLD entityLD: entities) {
//            try {
//                IoTStream ioTStream = IoTStream.fromEntity(entityLD);
//                ret.add(ioTStream);
//            } catch (Exception e){
//                LOGGER.error("Failed to create IoTStream of entity: {}", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
//        }

        //return ret;
    }


    @Override
    public List<Sensor> getSensors(Map<String, Object> query, int offset, int limit) throws Exception {
        List<Sensor> ret = new ArrayList<>();
        throw new NotImplementedException("");
//        GetEntitiesCommand command = new GetEntitiesCommand(Sensor.getTypeUri(), query, null, offset, limit);
//        List<EntityLD> entities = execute(command);
//
//        for(EntityLD entityLD: entities) {
//            try {
//                Sensor sensor = Sensor.fromEntity(entityLD);
//                ret.add(sensor);
//            } catch (Exception e){
//                LOGGER.error("Failed to create Sensor from entity: {}", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
//        }

        //return ret;
    }



    @Override
    public List<Platform> getPlatforms(Map<String, Object> query, int offset, int limit) throws Exception {
        List<Platform> ret = new ArrayList<>();
        throw new NotImplementedException("");

//        GetEntitiesCommand command = new GetEntitiesCommand(Platform.getTypeUri(), query, null, offset, limit);
//        List<EntityLD> entities = execute(command);
//
//        for(EntityLD entityLD: entities) {
//            try {
//                Platform ioTStream = Platform.fromEntity(entityLD);
//                ret.add(ioTStream);
//            } catch (Exception e){
//                LOGGER.error("Failed to create SosaPlatform from entity: {}", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
//        }
//
//        return ret;
    }

    @Override
    public List<ObservableProperty> getObservableProperties(Map<String, Object> query, int offset, int limit) throws Exception {
        List<ObservableProperty> ret = new ArrayList<>();
        throw new NotImplementedException("");

//        GetEntitiesCommand command = new GetEntitiesCommand(ObservableProperty.getTypeUri(), query, null, offset, limit);
//        List<EntityLD> entities = execute(command);
//
//        for(EntityLD entityLD: entities) {
//            try {
//                ObservableProperty ioTStream = ObservableProperty.fromEntity(entityLD);
//                ret.add(ioTStream);
//            } catch (Exception e){
//                LOGGER.error("Failed to create ObservableProperty from entity: {}", e.getLocalizedMessage());
//                e.printStackTrace();
//            }
//        }
//        return ret;
    }


    @Override
    public List<String> getEntityURIs(Map<String, Object> query, int offset, int limit) {
        throw new NotImplementedException("Not implemented");
    }


    @Override
    public List<EntityLD> getEntityById(String id) throws Exception {
        GetEntitiesCommand command = new GetEntitiesCommand(id, 0, 0);
        List<EntityLD> entities = execute(command);
        return entities;
    }


//    @Override
//    public List<EntityLD> getEntities(String entityType, Map<String, Object> query, int offset, int limit) throws Exception {
//        GetEntitiesCommand command = new GetEntitiesCommand(entityType, query, offset, limit);
//        List<EntityLD> entities = execute(command);
//        return entities;
//    }

    @Override
    public <T> List<T> getEntities(Class<T> targetClass, Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        throw new NotImplementedException("");
//        GetEntitiesCommand command = new GetEntitiesCommand(targetClass, query, null, offset, limit);
//        List<T> entities = execute(command, targetClass);
//        return entities;
    }

    @Override
    public String subscribeTo(Subscription subscription, Function<StreamObservation, Void> onChange) throws Exception {
        throw new NotImplementedException("");

    }

    @Override
    public List<EntityLD> getEntities(String entityType, Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        throw new NotImplementedException("");
//        GetEntitiesCommand command = new GetEntitiesCommand(entityType, query, ranking, offset, limit);
//        List<EntityLD> entities = execute(command);
//        return entities;
    }

//    @Override
//    public Boolean registerEntity(RDFModel model) throws Exception {
//        RegisterEntityCommand command = new RegisterEntityCommand(model);
//        return execute(command);
//    }

//    @Override
//    public List<StreamObservation> getObservations(String streamId,int offset, int limit) throws Exception {
//        throw new NotImplementedException("");
////        GetObservationsCommand command = new GetObservationsCommand(streamId, offset, limit);
////        return execute(command);
//    }

//    @Override
//    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
//        PushObservationsCommand command = new PushObservationsCommand(observations);
//        Boolean ret = execute(command);
//        LOGGER.info("Measuments pushed");
//        return ret;
//    }

//    @Override
//    public String subscribeTo(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {
//
//        SubscribeToEntityCommand command = new SubscribeToEntityCommand(entityId, attributes, notifyConditions, restriction);
//        String subsciptionId = execute(command);
//        final String exchangeName = subsciptionId;
//
//        LOGGER.debug("Creating queue for the {}", exchangeName);
//        String queueName = null;
//        try {
//            queueName = channel.queueDeclare().getQueue();
//            channel.queueBind(queueName, exchangeName, "");
//
//        }catch (Exception e){
//            throw new Exception("Failed to create a queue for subscription to "+entityId, e.getCause());
//        }
//
//        String finalQueueName = queueName;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String ctag = null;
//                String result2 = null;
//                LOGGER.debug("Subscribing to the queue {}", finalQueueName);
//                final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
//                try {
//                    ctag = channel.basicConsume(finalQueueName, true, (consumerTag, delivery) -> {
//                        if (delivery.getProperties().getCorrelationId().equals(subsciptionId)) {
//
//                            String body = new String(delivery.getBody());
//                            NotifyContextRequest notification = (NotifyContextRequest)NotifyContextRequest.parseStringToJson(body, NotifyContextRequest.class);
//                            String subscriptionId = notification.getSubscriptionId();
//                            List<ContextElementResponse> contextElementResponses = notification.getContextElementResponseList();
//                            ContextElementResponse contextElementResponse = contextElementResponses.get(0);
//                            ContextElement contextElement = contextElementResponse.getContextElement();
//
//                            //StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
//                            //onChange.apply(streamObservation);
//                            String abc = "123";
//                            //callback.accept(new String(delivery.getBody()));
//
//                            //onChange.apply();
//                            //response.offer(new String(delivery.getBody(), "UTF-8"));
//                        }
//                    }, consumerTag -> {});
//                    result2 = response.take();
//                    channel.basicCancel(ctag);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LOGGER.error("Failed to consume/cancel response: {}", e.getLocalizedMessage());
//                }
//            }
//        }).start();
//
//        return exchangeName;
//    }

    private <T> List<T> execute(GetEntitiesCommand command, Class<T> targetClass) throws Exception {
        List<EntityLD> entities = execute(command);
        List<T> ret = Utils.convertEntitiesToTargetClass(entities, targetClass);
        return ret;
    }

    private List<EntityLD> execute(GetEntitiesCommand command) throws Exception {
        List<EntityLD> ret = new ArrayList<>();
        String json = null;
        try {
            json = command.toJson();
        }
        catch (Exception e){
            LOGGER.error("Failed to covert command to json: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
        LOGGER.debug("Waiting response from orchestrator");

        byte[] response = rabbitRpcClient.request(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, json.getBytes());
        if(response==null)
            throw new Exception("Null response received");
        JsonArray result = (JsonArray) parseResponse(new String(response));

        LOGGER.debug("Creating instances of EntityLD");
        for (JsonElement element : (JsonArray) result) {
            try {
                EntityLD entityLD = EntityLD.fromJsonString(element.toString());
                ret.add(entityLD);
            } catch (Exception e) {
                LOGGER.error("Failed to convert json to EntityLD:" + e.getLocalizedMessage());
            }
        }

        return ret;
    }

    private Boolean execute(RegisterEntityCommand command) throws Exception{

        List<Exception> exceptions = new ArrayList<>();
        LOGGER.debug("Waiting response from orchestrator");
        byte[] response = rabbitRpcClient.request(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, command.toJson().getBytes());
        if(response==null)
            throw new Exception("Null response received");
        parseResponse(new String(response));
        return true;
    }

    private String execute(SubscribeToEntityCommand command) throws Exception{
        List<String> ret = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();
        LOGGER.debug("Waiting response from orchestrator");
        byte[] response = rabbitRpcClient.request(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, command.toJson().getBytes());

        if(response==null)
            throw new Exception("Null response received");
        JsonObject result = (JsonObject) parseResponse(new String(response));
        String subscriptionId = result.get("subscriptionId").getAsString();
        ret.add(subscriptionId);

        return ret.get(0);
    }

    private Boolean execute(PushObservationsCommand command) throws Exception{
        List<Exception> exceptions = new ArrayList<>();
        LOGGER.debug("Waiting response from orchestrator");
        byte[] response = rabbitRpcClient.request(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, command.toJson().getBytes());
        if(response==null)
            throw new Exception("Null response received");
        parseResponse(new String(response));
        return true;
    }

    private List<StreamObservation> execute(GetObservationsCommand command) throws Exception {
        List<StreamObservation> ret = new ArrayList<>();
        String json = null;
        try {
            json = command.toJson();
        }
        catch (Exception e){
            LOGGER.error("Failed to covert command to json: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
        LOGGER.debug("Waiting response from orchestrator");
        byte[] response = rabbitRpcClient.request(IOTCRAWLER_NOTIFICATIONS_EXCHANGE, json.getBytes());
        if(response==null)
            throw new Exception("Null response received");

        JsonArray result = (JsonArray) parseResponse(new String(response));
        LOGGER.debug("Deserealising StreamObservations");
        for (JsonElement element : (JsonArray) result) {
            try {
                StreamObservation entityLD = StreamObservation.fromJson(element.getAsString());
                ret.add(entityLD);
            } catch (Exception e) {
                LOGGER.error("Failed to convert json to EntityLD:" + e.getLocalizedMessage());
            }
        }

        return ret;
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
