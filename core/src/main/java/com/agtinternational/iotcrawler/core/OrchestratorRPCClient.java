package com.agtinternational.iotcrawler.core;

//import com.agtinternational.iotcrawler.core.models.NGSI_LD.EntityLD;
import com.agtinternational.iotcrawler.core.commands.*;
import com.agtinternational.iotcrawler.core.interfaces.AbstractIotCrawlerClient;
import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.apache.jena.atlas.iterator.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.COMMANDS_EXCHANGE_NAME;
import static com.agtinternational.iotcrawler.core.Constants.*;


//ToDo: timeout for requests

public class OrchestratorRPCClient implements AbstractIotCrawlerClient, AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(OrchestratorRPCClient.class);

    //Semaphore callFinishedMutex;

    Connection connection;
    Channel channel;
    JsonParser parser;
    String rabbitHost = "localhost";
    RabbitRpcClient rabbitRpcClient;
    Consumer consumer;

    @Override
    public void init() throws Exception {

        if(System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            rabbitHost = System.getenv(IOTCRAWLER_RABBIT_HOST);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);

        parser = new JsonParser();

        connection = factory.newConnection();
        channel = connection.createChannel();

        String queueName = channel.queueDeclare().getQueue();
        //channel.exchangeDeclare(COMMANDS_EXCHANGE_NAME, "fanout", false, true, null);
        channel.queueBind(queueName, COMMANDS_EXCHANGE_NAME, "");

        rabbitRpcClient = RabbitRpcClient.create(connection, COMMANDS_EXCHANGE_NAME);

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


        //callFinishedMutex = new Semaphore(0);
    }

    protected void handleCmd(byte bytes[], String replyTo) {

    }

    @Override
    public void run() throws Exception {

    }


    @Override
    public List<IoTStream> getStreams(int limit) throws Exception {
        final List<IoTStream> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(IoTStream.getTypeUri(), limit);

        final List<EntityLD> entities = execute(command);
        for(EntityLD entityLD: entities) {
            try {
                final IoTStream ioTStream = IoTStream.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create IoTStream of entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return ret;
    }


    @Override
    public List<IoTStream> getStreams(String query) throws Exception {
        List<IoTStream> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(IoTStream.getTypeUri(), query);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                IoTStream ioTStream = IoTStream.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create IoTStream of entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        return ret;
    }

    @Override
    public List<Sensor> getSensors(int limit) throws Exception {
        List<Sensor> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(Sensor.getTypeUri(), limit);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                Sensor sensor = Sensor.fromEntity(entityLD);
                ret.add(sensor);
            } catch (Exception e){
                LOGGER.error("Failed to create Sensor from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }

        }

        return ret;
    }

    @Override
    public List<Sensor> getSensors(String query) throws Exception {
        List<Sensor> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(Sensor.getTypeUri(), query);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                Sensor sensor = Sensor.fromEntity(entityLD);
                ret.add(sensor);
            } catch (Exception e){
                LOGGER.error("Failed to create Sensor from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        return ret;
    }

    @Override
    public List<IoTPlatform> getPlatforms(int limit) throws Exception {
        List<IoTPlatform> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(IoTPlatform.getTypeUri(), limit);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                IoTPlatform ioTStream = IoTPlatform.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create IoTPlatform from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }

        }
        return ret;
    }

    @Override
    public List<IoTPlatform> getPlatforms(String query) throws Exception {
        List<IoTPlatform> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(IoTPlatform.getTypeUri(), query);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                IoTPlatform ioTStream = IoTPlatform.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create IoTPlatform from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        return ret;
    }

    @Override
    public List<ObservableProperty> getObservableProperties(int limit) throws Exception {
        List<ObservableProperty> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(ObservableProperty.getTypeUri(), limit);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                ObservableProperty ioTStream = ObservableProperty.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create ObservableProperty from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }


        }

        return ret;
    }

    @Override
    public List<ObservableProperty> getObservableProperties(String query) throws Exception {
        List<ObservableProperty> ret = new ArrayList<>();

        GetEntityCommand command = new GetEntityCommand(ObservableProperty.getTypeUri(), query);
        List<EntityLD> entities = execute(command);

        for(EntityLD entityLD: entities) {
            try {
                ObservableProperty ioTStream = ObservableProperty.fromEntity(entityLD);
                ret.add(ioTStream);
            } catch (Exception e){
                LOGGER.error("Failed to create ObservableProperty from entity: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return ret;
    }


    @Override
    public List<String> getEntityURIs(String query) {
        throw new NotImplementedException();
    }

    @Override
    public List<EntityLD> getEntities(String entityType, int limit) throws Exception {
        GetEntityCommand command = new GetEntityCommand(entityType, limit);
        List<EntityLD> entities = execute(command);
        return entities;
    }

    @Override
    public Boolean registerEntity(RDFModel model) throws Exception {
        RegisterEntityCommand command = new RegisterEntityCommand(model);
        return execute(command);
    }

    @Override
    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception {
        GetObservationsCommand command = new GetObservationsCommand(streamId, limit);
        return execute(command);
    }

    @Override
    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
        PushObservationsCommand command = new PushObservationsCommand(observations);
        Boolean ret = execute(command);
        LOGGER.info("Measuments pushed");
        return ret;
    }

    @Override
    public String subscribeTo(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {

        SubscribeToEntityCommand command = new SubscribeToEntityCommand(entityId, attributes, notifyConditions, restriction);
        String subsciptionId = execute(command);
        final String exchangeName = subsciptionId;

        LOGGER.debug("Creating queue for the {}", exchangeName);
        String queueName = null;
        try {
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, "");

        }catch (Exception e){
            throw new Exception("Failed to create a queue for subscription to "+entityId, e.getCause());
        }

        String finalQueueName = queueName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ctag = null;
                String result2 = null;
                LOGGER.debug("Subscribing to the queue {}", finalQueueName);
                final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
                try {
                    ctag = channel.basicConsume(finalQueueName, true, (consumerTag, delivery) -> {
                        if (delivery.getProperties().getCorrelationId().equals(subsciptionId)) {

                            String body = new String(delivery.getBody());
                            NotifyContextRequest notification = (NotifyContextRequest)NotifyContextRequest.parseStringToJson(body, NotifyContextRequest.class);
                            String subscriptionId = notification.getSubscriptionId();
                            List<ContextElementResponse> contextElementResponses = notification.getContextElementResponseList();
                            ContextElementResponse contextElementResponse = contextElementResponses.get(0);
                            ContextElement contextElement = contextElementResponse.getContextElement();
                            StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
                            onChange.apply(streamObservation);
                            String abc = "123";
                            //callback.accept(new String(delivery.getBody()));

                            //onChange.apply();
                            //response.offer(new String(delivery.getBody(), "UTF-8"));
                        }
                    }, consumerTag -> {});
                    result2 = response.take();
                    channel.basicCancel(ctag);
                } catch (Exception e) {
                    LOGGER.error("Failed to consume/cancel response: {}", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }).start();

        return exchangeName;
    }


    private List<EntityLD> execute(GetEntityCommand command) throws Exception {
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
        byte[] response = rabbitRpcClient.request(COMMANDS_EXCHANGE_NAME, json.getBytes());
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
        byte[] response = rabbitRpcClient.request(COMMANDS_EXCHANGE_NAME, command.toJson().getBytes());
        if(response==null)
            throw new Exception("Null response received");
        parseResponse(new String(response));
        return true;
    }

    private String execute(SubscribeToEntityCommand command) throws Exception{
        List<String> ret = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();
        LOGGER.debug("Waiting response from orchestrator");
        byte[] response = rabbitRpcClient.request(COMMANDS_EXCHANGE_NAME, command.toJson().getBytes());

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
        byte[] response = rabbitRpcClient.request(COMMANDS_EXCHANGE_NAME, command.toJson().getBytes());
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
        byte[] response = rabbitRpcClient.request(COMMANDS_EXCHANGE_NAME, json.getBytes());
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
