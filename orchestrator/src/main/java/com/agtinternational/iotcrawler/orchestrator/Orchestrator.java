package com.agtinternational.iotcrawler.orchestrator;

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.orchestrator.clients.AbstractDataClient;
import com.agtinternational.iotcrawler.orchestrator.clients.AbstractMetadataClient;
import com.agtinternational.iotcrawler.orchestrator.clients.IotBrokerDataClient;
import com.agtinternational.iotcrawler.orchestrator.clients.NgsiLD_MdrClient;
import com.agtinternational.iotcrawler.core.commands.*;
import com.agtinternational.iotcrawler.core.models.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.rabbitmq.client.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class Orchestrator extends IotCrawlerClient {
    private Logger LOGGER = LoggerFactory.getLogger(Orchestrator.class);

    String rabbitHost = "localhost";
    String redisHost = "localhost";
    String endpoint = "/notify";

    //AbstractMetadataClient metadataClient;
    AbstractMetadataClient metadataClient;
    AbstractDataClient dataBrokerClient;
    HttpServer httpServer;
    Semaphore httpServiceFinishedMutex = new Semaphore(0);
    JsonParser parser = new JsonParser();

    Channel channel;
    Map<String, String> rpcSubscriptionRequests = new HashMap<>();
    Map<String, Function<StreamObservation, Void>> streamObservationHandlers = new HashMap<>();
    //Map<String, Function<String, Void>> rpcSubscriptions = new HashMap<>();
    //List<String> rpcSubscriptions = new ArrayList<>();
    Map<Pair<String, String[]>, Function<StreamObservation, Void>> knownSubscriptions = new HashMap<>();

    RedisClient redisClient;
    StatefulRedisConnection<String, String> redisConnection;
    RedisCommands<String, String> redisSyncCommands;

    private boolean serverStarted;
    private Connection connection;


    public Orchestrator() {

    }

    public static void main(String[] args) throws Exception {
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.init();
        orchestrator.run();
    }

    public void init() throws Exception {

        //metadataClient = new TripleStoreMDRClient();
        httpServer = new HttpServer();
        metadataClient = new NgsiLD_MdrClient();
        dataBrokerClient = new IotBrokerDataClient();

        if (System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            rabbitHost = System.getenv(IOTCRAWLER_RABBIT_HOST);

        if (System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            redisHost = System.getenv(IOTCRAWLER_REDIS_HOST);




    }

//    Function<String, Void> createNotifyCallback(String subscriptionId) {
//        return new Function<String, Void>() {
//            @Override
//            public Void apply(String notification) {
//
//                AMQP.BasicProperties props = new AMQP.BasicProperties
//                        .Builder()
//                        .correlationId(subscriptionId)
//                        .build();
//
//                try {
//                    channel.basicPublish(subscriptionId, "", props, notification.getBytes("UTF-8"));
//                } catch (IOException e) {
//                    LOGGER.error("Failed to publish to channel: {}", e.getLocalizedMessage());
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//        };
//    }



    public void receiveCommand(byte[] data, AMQP.BasicProperties properties){

        String message = new String(data);
        JsonObject messageObj = (JsonObject)parser.parse(message);
        String reply = null;

        String command0 = messageObj.get("command").getAsString();

        Exception exception = null;
        //LOGGER.debug("receiveCommand "+command);

        if(command0.equals(GetEntitiesCommand.class.getSimpleName())){
            GetEntitiesCommand command = null;
            try {
                command = GetEntitiesCommand.fromJson(message);
            }
            catch (Exception e){
                exception = new Exception("Failed to parse command from "+messageObj+": "+e.getLocalizedMessage(), e);
            }

            int limit = command.getLimit();

            List<EntityLD> entities = null;
            if(command!=null)
            try {
                if(command.getIds()!=null) {
                    //Class targetClass = Utils.getTargetClass(command.getTypeURI());
                    entities = metadataClient.getEntities(command.getIds(), command.getTypeURI());
                }else {
                    entities = metadataClient.getEntities(command.getTypeURI(), limit);
                    JsonArray jsonArray = EntitiesToJson(entities);
                }
            }
            catch (Exception e){
                exception = new Exception("Failed to get entities: "+e.getLocalizedMessage(), e);
            }

            if(entities!=null){
                JsonArray jsonArray = EntitiesToJson(entities);
                reply = jsonArray.toString();
            }

        }else if(command0.equals(RegisterEntityCommand.class.getSimpleName())){
			LOGGER.debug(message);
            RegisterEntityCommand registerEntityCommand=null;
            try {
                registerEntityCommand = RegisterEntityCommand.fromJson(message);
            }
            catch (Exception e){
                exception = new Exception("Failed to parse command from "+messageObj+": "+e.getLocalizedMessage(), e);
            }
            if(registerEntityCommand!=null)
            try {
                Boolean result = registerEntity(registerEntityCommand.getModel());
                reply = "{ success: " + result + " }";
            }
            catch (Exception e){
                exception = new Exception("Failed to register entity: "+e.getLocalizedMessage(), e.getCause());

            }
        }else if(command0.equals(GetObservationsCommand.class.getSimpleName())){

            GetObservationsCommand command1 = null;
            try {
                command1 = GetObservationsCommand.fromJson(message);
            }
            catch (Exception e){
                exception = new Exception("Failed to parse command from "+message+": "+e.getLocalizedMessage(), e.getCause());
            }
            if(command1!=null)
                try {
                    List<StreamObservation> result = getObservations(command1.getStreamId(), command1.getLimit());
                    reply =  ObservationsToJson(result).toString();
                }
                catch (Exception e){
                    exception = new Exception("Failed to get observations: "+e.getLocalizedMessage(), e);
                }
        }else if(command0.equals(PushObservationsCommand.class.getSimpleName())){

            PushObservationsCommand command1 = null;
            try {
                command1 = PushObservationsCommand.fromJson(message);
            }
            catch (Exception e){
                exception = new Exception("Failed to parse command from json: "+ e.getLocalizedMessage(), e); //new Exception("Failed to parse "+command+" from json", e.getCause());
            }
            if(command1!=null)
                try {
                    Boolean result = pushObservationsToBroker(command1.getObservations());
                    reply = "{ success: " + result + " }";
                }
                catch (Exception e){
                    exception = new Exception("Failed to push observations: "+e.getLocalizedMessage(), e);
                }
        }else if(command0.equals(SubscribeToEntityCommand.class.getSimpleName())){
            String arguments = messageObj.get("args").toString();
            if(redisSyncCommands!=null && redisSyncCommands.hget("rpcSubscriptionRequests", arguments)!=null) {
                String subcriptionId = redisSyncCommands.hget("rpcSubscriptionRequests", arguments);
                LOGGER.debug("Subscription with requested arguments was found in redis ({}). Skipping new subscription", subcriptionId);
                try {
                    channel.exchangeDeclare(subcriptionId, "fanout");
                }
                catch (Exception e){
                    exception = new Exception("Failed to register exchange for "+subcriptionId+":"+e.getLocalizedMessage(), e);
                }

                reply = "{ 'subscriptionId': '" +subcriptionId +"' }";
            }else {
                SubscribeToEntityCommand command1 = null;
                try {
                    command1 = SubscribeToEntityCommand.fromJson(message);
                }
                catch (Exception e){
                    exception = new Exception("Failed to parse command from json: "+ e.getLocalizedMessage(), e); //new Exception("Failed to parse "+command+" from json", e.getCause());
                }
                String subscriptionId = null;

                if(command1!=null) {
                    try {
                        subscriptionId = subscribeTo(command1.getEntityId(), command1.getAttributes(), command1.getNotifyConditions(), command1.getRestriction());
                    } catch (Exception e) {
                        e.printStackTrace();
                        exception = new Exception("Failed to create a subscription to " + command1.getEntityId() + ": " + e.getLocalizedMessage(), e);
                    }
                }
                if(subscriptionId!=null) {
                    try {
                        final String exchangeName = subscriptionId;
                        channel.exchangeDeclare(exchangeName, "fanout");

                        //Function<String, Void> notifyCallback = createNotifyCallback(exchangeName);

                        //rpcSubscriptions.add(subscriptionId);
                        //rpcSubscriptionRequests.put(arguments, subscriptionId);

                        //                    List<String> names = new ArrayList<>();
                        //                    names.add(command1.getEntityId());
                        //                    names.addAll(Arrays.asList(command1.getAttributes()));
                        //                    String args = String.join("_", names);
                        if (redisSyncCommands != null) {
                            redisSyncCommands.hset("rpcSubscriptionIds", subscriptionId, subscriptionId);
                            redisSyncCommands.hset("rpcSubscriptionRequests", arguments, subscriptionId);
                        }
                        //reply = "{ 'subscriptionId': '" + subscriptionId + "', 'queueName': '"+queueName+"' }";
                        reply = "{ 'subscriptionId': '" + subscriptionId + "' }";
                    } catch (Exception e) {
                        exception = new Exception("Failed to register exchange for " + subscriptionId + ":" + e.getLocalizedMessage(), e);
                    }
                }
            }
        }else
            exception = new Exception("No handler for the command: \""+command0+"\"");

        if(exception!=null) {
            exception.printStackTrace();
            JsonObject replyObj = new JsonObject();
            replyObj.addProperty("error", exception.getLocalizedMessage());
            reply = replyObj.toString();
        }
        else if(reply==null) {
            LOGGER.error("Reply is not defined");
            reply = "{ error: \"Reply is not defined\"}";
        }
        LOGGER.debug("Publishing result to {}",properties.getReplyTo());
        try {
            channel.basicPublish("", properties.getReplyTo(), properties, reply.getBytes("UTF-8"));
        }
        catch (Exception e){
            LOGGER.error("Failed to respond to RPC request", e.getLocalizedMessage());
            e.getLocalizedMessage();
        }
    }

    private JsonArray EntitiesToJson(List<EntityLD> entities){
        JsonArray ret = new JsonArray();
        for(EntityLD entity: entities)
        try{
            JsonObject jsonObject = entity.toJsonObject();
            ret.add (jsonObject);
        }
        catch (Exception e){
            LOGGER.error("Failed to convert entity to json {}", entity.getId());
        }
        return ret;
    }

    private JsonArray ObservationsToJson(List<StreamObservation> streamObservations){
        JsonArray ret = new JsonArray();
        for(StreamObservation streamObservation: streamObservations)
            try{
                String jsonLDString = streamObservation.toJsonLDString();
                ret.add (jsonLDString);
            }
            catch (Exception e){
                LOGGER.error("Failed to convert entity to json {}", streamObservation.getURI());
            }
        return ret;
    }



    public void run() throws Exception {
        LOGGER.info("Starting orchestrator");

        LOGGER.info("Syncing with Redis at {}", "redis://"+redisHost);
        redisClient = RedisClient.create("redis://"+redisHost);
        //redisConnection = redisClient.connect();
        //redisSyncCommands = redisConnection.sync();

        LOGGER.info("Connecting to RabbitMQ at {}", rabbitHost);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);

        int attempt = 0;
        while(connection==null) {
            attempt++;
            try {
                LOGGER.debug("Trying to connect to rabbit (Attempt {} of {})", attempt, 12);
                connection = factory.newConnection();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        if(connection!=null) {

            String queueName = null;
            try {
                channel = connection.createChannel();
                channel.exchangeDeclare(IOTCRAWLER_COMMANDS_EXCHANGE, "fanout");
                queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, IOTCRAWLER_COMMANDS_EXCHANGE, "");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        receiveCommand(body, properties);
                    }
                };
                channel.basicConsume(queueName, true, consumer);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Failed to create rabbit channel/consumer");
            }
            LOGGER.info(" [*] Waiting for RPC messages also from Rabbit");
        }





        initHttpServer();
        startHttpServer();
    }

    private void initHttpServer(){
        LOGGER.info("Initializing web server");

        try {
            httpServer.init();
        }
        catch (Exception e){
            LOGGER.error("Failed to init http server: {}", e.getLocalizedMessage());
            httpServer = null;
            return;
        }

        Function<String, Void> notificationReceivedHandler = new Function<String, Void>() {
            @Override
            public Void apply(String theString) {
                if(theString.length()==0)
                    return null;


                NotifyContextRequest notification = (NotifyContextRequest)NotifyContextRequest.parseStringToJson(theString, NotifyContextRequest.class);
                String subscriptionId = notification.getSubscriptionId();

                if(streamObservationHandlers.containsKey(subscriptionId)) {
                    List<ContextElementResponse> contextElementResponses = notification.getContextElementResponseList();
                    ContextElementResponse contextElementResponse = contextElementResponses.get(0);
                    ContextElement contextElement = contextElementResponse.getContextElement();
                    StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
                    streamObservationHandlers.get(subscriptionId).apply(streamObservation);
                }else{
                    //if(redisSyncCommands.hget("rpcSubscriptionIds", subscriptionId)!=null)
                    AMQP.BasicProperties props = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(subscriptionId)
                            .build();
//					try {
//							channel.basicPublish(subscriptionId, "", props, theString.getBytes("UTF-8"));
//							//published = true;
//						} catch (IOException e) {
//							LOGGER.error("Failed to publish to channel: {}", e.getLocalizedMessage());
//							e.printStackTrace();
//						}
//
					Boolean published = false;
					while(!published){
						try {
                            channel.basicPublish(subscriptionId, "", props, theString.getBytes("UTF-8"));
							published = true;
						} catch (IOException e) {
							e.printStackTrace();
                            LOGGER.error("Failed to publish notification {} to a rabbitMQ channel: {}", subscriptionId, e.getLocalizedMessage());
						}

						if(!published)
                            try{
                                LOGGER.debug("Reopening channel");
                                channel = connection.createChannel();
                                LOGGER.debug("Redeclaring exchange {}", subscriptionId);
                                channel.exchangeDeclare(subscriptionId, "fanout");
                                LOGGER.debug("Trying to publish into exchange {}", subscriptionId);
                                channel.basicPublish(subscriptionId, "", props, theString.getBytes("UTF-8"));
                                published = true;
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                                LOGGER.error("Failed to publish to channel: {}", e.getLocalizedMessage());
                            }
					}
                }
//                else {
//                    LOGGER.warn("Failed to find handler for subscription id: {}. Deleting subscription", subscriptionId);
//                    try {
//                        dataBrokerClient.deleteSubscription(subscriptionId);
//                    }catch (Exception e){
//                        LOGGER.error(e.getLocalizedMessage());
//                        e.printStackTrace();
//                    }
//                }

                return null;
            }
        };


        httpServer.addContext(endpoint, new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                // parse request
                LOGGER.info("POST request received");
                String response="";
                String theString = null;
                try {
                    InputStream is = he.getRequestBody();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, Charset.defaultCharset());
                    theString = writer.toString();

                    // send response
                    response= "Received: "+theString;
                    //receivings.add(theString);
                    LOGGER.info(response);
                }
                catch (Exception e){
                    response = "Error: "+e.getLocalizedMessage();
                    LOGGER.error("Failed to read the message: {}", e.getLocalizedMessage());
                }

                if(theString!=null) {
                    try {
                        notificationReceivedHandler.apply(theString);
                    } catch (Exception e) {
                        LOGGER.error("Failed to apply handler on message: {}", e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }

                Map<String, Object> parameters = new HashMap<String, Object>();
                for (String key : parameters.keySet())
                    response += key + " = " + parameters.get(key) + "\n";
                he.sendResponseHeaders(200, response.length());

                OutputStream os = he.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();

            }
        });


    }

    private void startHttpServer(){
        if(httpServer==null){
            LOGGER.info("Starting http server skipped");
            return;
        }
        LOGGER.info("Starting built-in web server");
        try {
            httpServer.start();
            serverStarted = true;
            httpServiceFinishedMutex.acquire();
        } catch (Exception e) {
            LOGGER.error("Failed to start web server: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }


    }


    public void processApplicationRequirements(){
        //1) List of IoTstreams to subscribe (maybe a graph structure)
        //2) Init subscriptions
        //3) Monitoring?
    }

    public void createEndpointDescription(String brokerName, IoTStream ioTStream){
        // create IoTBroker Endpoint descriptions
        //createEndpointDescription

    }



//    public com.agt.smartHomeCrawler.core.MetadataProvider getMetadataProvider() {
//        return metadataProvider;
//    }



//    private void updateContext(List<ContextElement> contextElements){
//
//        UpdateContextRequest request = new UpdateContextRequest();
//        request.setContextElement(contextElements);
//        request.setUpdateAction(UpdateActionType.APPEND);
//        UpdateContextResponse response = iotBrokerClient.updateContext(request, URI.create(serverUrl));
//        if(response.getErrorCode().getCode()!=200) {
//            LOGGER.error(response.getErrorCode().toJsonString());
//        }
//
//    }


    public Boolean registerStream(IoTStream ioTStream) throws Exception {
       return metadataClient.registerEntity(ioTStream);
    }

    public Boolean registerEntity(RDFModel model) throws Exception {
       return metadataClient.registerEntity(model);
    }

    @Override
    public List<EntityLD> getEntities(String[] ids, String targetClass) throws Exception {
        return metadataClient.getEntities(ids, targetClass);
    }

    @Override
    public List<EntityLD> getEntities(String entityType, int limit) throws Exception {
        return metadataClient.getEntities(entityType, limit);
    }

    @Override
    public List<EntityLD> getEntities(String entityType, String query) throws Exception {
        return metadataClient.getEntities(entityType, query);
    }

    @Override
    public List<String> getEntityURIs(String query) {
        return metadataClient.getEntityURIs(query);
    }

    //@Override
//    public <T> List<T> getEntities(Class<T> targetClass, int limit) throws Exception {
//        return metadataClient.getEntities(targetClass, limit);
//    }
//
//    @Override
//    public <T> List<T> getEntities(Class<T> targetClass, String query) {
//        return metadataClient.getEntities(targetClass, query);
//    }

//    @Override
//    public List<RDFModel> getEntities(FilteringSelector selector, int limit) {
//        return metadataClient.getEntities(selector, limit);
//    }

//    public List<IoTStream> getStreams(int limit) throws Exception {
//        return metadataClient.getEntities(IoTStream.class, limit);
//    }


//    public List<IoTStream> getStreams(String query){
//        return metadataClient.getEntities(IoTStream.class, query);
//    }
//
//    public List<IoTStream> getStreams(String[] ids) throws Exception {
//        return metadataClient.getEntities(ids, IoTStream.class);
//    }
//
//    public List<Sensor> getSensors(int limit) throws Exception {
//        return metadataClient.getEntities(Sensor.class, limit);
//    }
//
//    @Override
//    public List<Sensor> getSensors(String query){
//        return metadataClient.getEntities(Sensor.class, query);
//    }
//
//    @Override
//    public List<SosaPlatform> getPlatforms(int limit) throws Exception {
//        return metadataClient.getEntities(SosaPlatform.class, limit);
//    }
//
//    @Override
//    public List<SosaPlatform> getPlatforms(String query) {
//        return metadataClient.getEntities(SosaPlatform.class, query);
//    }
//
//    @Override
//    public List<ObservableProperty> getObservableProperties(int limit) throws Exception {
//        return metadataClient.getEntities(ObservableProperty.class, limit);
//    }
//
//    @Override
//    public List<ObservableProperty> getObservableProperties(String query) {
//        return metadataClient.getEntities(ObservableProperty.class, query);
//    }



    //  subscribing to a data client (IoT Broker, not to MDR)
    @Override
    public String subscribeTo(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {

        String subscriptionId = subscribeTo(entityId, attributes, notifyConditions, restriction);
        streamObservationHandlers.put(subscriptionId, onChange);
//        if(!serverStarted)
//            startHttpServer();
        return subscriptionId;
    }

    private String subscribeTo(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction) throws Exception {
        List<String> attrs2 = new ArrayList<>();
        for(String attribute: attributes)
            attrs2.add( URI.create(attribute).getFragment()!=null?URI.create(attribute).getFragment():attribute);

        String reference = httpServer.getUrl()+ endpoint;

        LOGGER.debug("Subscribing to ({}) with a reference {}", entityId, reference);
        String subscriptionId = dataBrokerClient.subscribeTo(entityId, attributes, reference, notifyConditions, restriction);
        return subscriptionId;
    }

    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
        Boolean ret = dataBrokerClient.pushObservations(observations);
        return ret;
    }

//    @Override
//    public List<StreamObservation> getObservations() throws Exception {
//        return null;
//    }
//
//    @Override
//    public List<StreamObservation> getObservations(int i) throws Exception {
//        return null;
//    }

    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception {
        List<StreamObservation> ret = dataBrokerClient.getObservations(streamId, limit);
        return ret;
    }

//    public List<StreamObservation> getObservations(int limit) throws Exception {
//
//        return dataBrokerClient.getObservations(limit);
//    }


    @Override
    public void close() throws IOException {
        httpServer.stop();
    }
}
