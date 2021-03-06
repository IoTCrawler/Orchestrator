//package com.agtinternational.iotcrawler.core.clients;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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
//
//import com.agtinternational.iotcrawler.core.Utils;
//import com.agtinternational.iotcrawler.core.commands.GetEntitiesCommand;
//import com.agtinternational.iotcrawler.core.commands.GetObservationsCommand;
//import com.agtinternational.iotcrawler.core.commands.PushObservationsCommand;
//import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
//import com.agtinternational.iotcrawler.core.models.*;
//import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
//import com.agtinternational.iotcrawler.fiware.models.EntityLD;
//import com.agtinternational.iotcrawler.fiware.models.subscription.SubscriptionLD;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.orange.ngsi2.model.Paginated;
//import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
//import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
//import org.apache.commons.lang3.NotImplementedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public class IoTCrawlerCombinedClient {
//
//
//
//    //ToDo: timeout for requests
////@RunWith(Parameterized.class)
//    public class IoTCrawlerRESTClient extends IotCrawlerClient implements AutoCloseable {
//        private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);
//
//        //Semaphore callFinishedMutex;
//
//        JsonParser parser;
//        String orchestratorUrl = "http://localhost:3001/ngsi-ld/";
//        NgsiLDClient ngsiLDClient;
//        //RabbitRpcClient rabbitRpcClient;
//        //Consumer consumer;
//
//        public IoTCrawlerRESTClient(String url){
//            LOGGER.info("Initializing IoTCrawlerRESTClient client to {}", url);
//            orchestratorUrl = url;
//            ngsiLDClient = new NgsiLDClient(orchestratorUrl);
//        }
//
//        @Override
//        public void init(){
//
//
//
//        }
//
//
//        @Override
//        public void run() throws Exception {
//
//        }
//
//
//        @Override
//        public List<IoTStream> getStreams(String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(Utils.cutURL(IoTStream.getTypeUri(), RDFModel.getNamespaces())),  null, query,null, null,  offset, limit, false).get();
//            List<EntityLD> entities = paginated.getItems();
//            List<IoTStream> ret =  Utils.convertEntitiesToTargetClass(entities, IoTStream.class);
//            return ret;
//        }
//
//
//        @Override
//        public List<Sensor> getSensors(String query, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(Utils.cutURL(Sensor.getTypeUri(), RDFModel.getNamespaces())),  null, query,null, null,  offset, limit, false).get();
//            List<EntityLD> entities = paginated.getItems();
//            List<Sensor> ret =  Utils.convertEntitiesToTargetClass(entities, Sensor.class);
//            return ret;
//        }
//
//
//
//        @Override
//        public List<Platform> getPlatforms(String query, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(Utils.cutURL(Platform.getTypeUri(), RDFModel.getNamespaces())),  null, query,null, null,  offset, limit, false).get();
//            List<EntityLD> entities = paginated.getItems();
//            List<Platform> ret =  Utils.convertEntitiesToTargetClass(entities, Platform.class);
//            return ret;
//        }
//
//        @Override
//        public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(Utils.cutURL(ObservableProperty.getTypeUri(), RDFModel.getNamespaces())),  null, query,null, null,  offset, limit, false).get();
//            List<EntityLD> entities = paginated.getItems();
//            List<ObservableProperty> ret =  Utils.convertEntitiesToTargetClass(entities, ObservableProperty.class);
//            return ret;
//        }
//
//
//        @Override
//        public List<String> getEntityURIs(String query, int offset, int limit) {
//            throw new NotImplementedException("Not implemented");
//        }
//
//
//        @Override
//        public List<EntityLD> getEntitiesById(String[] ids, String entityType) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(Arrays.asList(ids), null, Arrays.asList(Utils.cutURL(entityType, RDFModel.getNamespaces())),  null, null,null, null, 0, 0, false).get();
//            List<EntityLD> entities = paginated.getItems();
//
//            return entities;
//        }
//
//
////    @Override
////    public List<EntityLD> getEntities(String entityType, String query, int offset, int limit) throws Exception {
////        GetEntitiesCommand command = new GetEntitiesCommand(entityType, query, offset, limit);
////        List<EntityLD> entities = execute(command);
////        return entities;
////    }
//
//        @Override
//        public <T> List<T> getEntities(Class<T> targetClass, String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(Utils.getTypeURI(targetClass)),  null, query,null, null, offset, limit, false).get();
//            List<EntityLD> entities0 = paginated.getItems();
//
//            List<T> entities = Utils.convertEntitiesToTargetClass(entities0, targetClass);
//            return entities;
//        }
//
//
//
//        @Override
//        public List<EntityLD> getEntities(String entityType, String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
//            Paginated<EntityLD> paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(entityType),  null, query,null, null, offset, limit, false).get();
//            List<EntityLD> entities = paginated.getItems();
//
//            return entities;
//        }
//
////    @Override
////    public Boolean registerEntity(RDFModel rdfModel) throws Exception {
////        ListenableFuture<Void> ret = ngsiLDClient.addEntity(rdfModel.toEntityLD(true));
////        throw new NotImplementedException("");
////
////    }
//
//        @Override
//        public List<StreamObservation> getObservations(String streamId,int offset, int limit) throws Exception {
//            GetObservationsCommand command = new GetObservationsCommand(streamId, offset, limit);
//            throw new NotImplementedException("");
//        }
//
////        @Override
////        public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
////            PushObservationsCommand command = new PushObservationsCommand(observations);
////
////            LOGGER.info("Measuments pushed");
////            throw new NotImplementedException("");
////        }
//        @Override
//        public String subscribeTo(SubscriptionLD subscription, Function<StreamObservation, Void> onChange) throws Exception {
//            throw new NotImplementedException("");
//            //return null;
//        }
//
////        @Override
////        public String subscribeTo(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {
////            throw new NotImplementedException("");
//////        SubscribeToEntityCommand command = new SubscribeToEntityCommand(entityId, attributes, notifyConditions, restriction);
//////        String subsciptionId = execute(command);
//////        final String exchangeName = subsciptionId;
//////
//////        LOGGER.debug("Creating queue for the {}", exchangeName);
//////        String queueName = null;
//////        try {
//////            queueName = channel.queueDeclare().getQueue();
//////            channel.queueBind(queueName, exchangeName, "");
//////
//////        }catch (Exception e){
//////            throw new Exception("Failed to create a queue for subscription to "+entityId, e.getCause());
//////        }
//////
//////        String finalQueueName = queueName;
//////        new Thread(new Runnable() {
//////            @Override
//////            public void run() {
//////                String ctag = null;
//////                String result2 = null;
//////                LOGGER.debug("Subscribing to the queue {}", finalQueueName);
//////                final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
//////                try {
//////                    ctag = channel.basicConsume(finalQueueName, true, (consumerTag, delivery) -> {
//////                        if (delivery.getProperties().getCorrelationId().equals(subsciptionId)) {
//////
//////                            String body = new String(delivery.getBody());
//////                            NotifyContextRequest notification = (NotifyContextRequest)NotifyContextRequest.parseStringToJson(body, NotifyContextRequest.class);
//////                            String subscriptionId = notification.getSubscriptionId();
//////                            List<ContextElementResponse> contextElementResponses = notification.getContextElementResponseList();
//////                            ContextElementResponse contextElementResponse = contextElementResponses.get(0);
//////                            ContextElement contextElement = contextElementResponse.getContextElement();
//////                            StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
//////                            onChange.apply(streamObservation);
//////                            String abc = "123";
//////                            //callback.accept(new String(delivery.getBody()));
//////
//////                            //onChange.apply();
//////                            //response.offer(new String(delivery.getBody(), "UTF-8"));
//////                        }
//////                    }, consumerTag -> {});
//////                    result2 = response.take();
//////                    channel.basicCancel(ctag);
//////                } catch (Exception e) {
//////                    e.printStackTrace();
//////                    LOGGER.error("Failed to consume/cancel response: {}", e.getLocalizedMessage());
//////                }
//////            }
//////        }).start();
//////
//////        return exchangeName;
////        }
//
//        private <T> List<T> execute(GetEntitiesCommand command, Class<T> targetClass) throws Exception {
//            throw new NotImplementedException("");
////        List<EntityLD> entities = execute(command);
////        List<T> ret = Utils.convertEntitiesToTargetClass(entities, targetClass);
////        return ret;
//        }
//
////    private List<EntityLD> execute(GetEntitiesCommand command) throws Exception {
////        List<EntityLD> ret = new ArrayList<>();
////        String json = null;
////        try {
////            json = command.toJson();
////        }
////        catch (Exception e){
////            LOGGER.error("Failed to covert command to json: {}", e.getLocalizedMessage());
////            e.printStackTrace();
////        }
////        LOGGER.debug("Waiting response from orchestrator");
////
////        byte[] response = rabbitRpcClient.request(IOTCRAWLER_COMMANDS_EXCHANGE, json.getBytes());
////        if(response==null)
////            throw new Exception("Null response received");
////        JsonArray result = (JsonArray) parseResponse(new String(response));
////
////        LOGGER.debug("Creating instances of EntityLD");
////        for (JsonElement element : (JsonArray) result) {
////            try {
////                EntityLD entityLD = EntityLD.fromJsonString(element.toString());
////                ret.add(entityLD);
////            } catch (Exception e) {
////                LOGGER.error("Failed to convert json to EntityLD:" + e.getLocalizedMessage());
////            }
////        }
////
////        return ret;
////    }
////
////    private Boolean execute(RegisterEntityCommand command) throws Exception{
////
////        List<Exception> exceptions = new ArrayList<>();
////        LOGGER.debug("Waiting response from orchestrator");
////        byte[] response = rabbitRpcClient.request(IOTCRAWLER_COMMANDS_EXCHANGE, command.toJson().getBytes());
////        if(response==null)
////            throw new Exception("Null response received");
////        parseResponse(new String(response));
////        return true;
////    }
////
////    private String execute(SubscribeToEntityCommand command) throws Exception{
////        List<String> ret = new ArrayList<>();
////        List<Exception> exceptions = new ArrayList<>();
////        LOGGER.debug("Waiting response from orchestrator");
////        byte[] response = rabbitRpcClient.request(IOTCRAWLER_COMMANDS_EXCHANGE, command.toJson().getBytes());
////
////        if(response==null)
////            throw new Exception("Null response received");
////        JsonObject result = (JsonObject) parseResponse(new String(response));
////        String subscriptionId = result.get("subscriptionId").getAsString();
////        ret.add(subscriptionId);
////
////        return ret.get(0);
////    }
////
////    private Boolean execute(PushObservationsCommand command) throws Exception{
////        List<Exception> exceptions = new ArrayList<>();
////        LOGGER.debug("Waiting response from orchestrator");
////        byte[] response = rabbitRpcClient.request(IOTCRAWLER_COMMANDS_EXCHANGE, command.toJson().getBytes());
////        if(response==null)
////            throw new Exception("Null response received");
////        parseResponse(new String(response));
////        return true;
////    }
////
////    private List<StreamObservation> execute(GetObservationsCommand command) throws Exception {
////        List<StreamObservation> ret = new ArrayList<>();
////        String json = null;
////        try {
////            json = command.toJson();
////        }
////        catch (Exception e){
////            LOGGER.error("Failed to covert command to json: {}", e.getLocalizedMessage());
////            e.printStackTrace();
////        }
////        LOGGER.debug("Waiting response from orchestrator");
////        byte[] response = rabbitRpcClient.request(IOTCRAWLER_COMMANDS_EXCHANGE, json.getBytes());
////        if(response==null)
////            throw new Exception("Null response received");
////
////        JsonArray result = (JsonArray) parseResponse(new String(response));
////        LOGGER.debug("Deserealising StreamObservations");
////        for (JsonElement element : (JsonArray) result) {
////            try {
////                StreamObservation entityLD = StreamObservation.fromJson(element.getAsString());
////                ret.add(entityLD);
////            } catch (Exception e) {
////                LOGGER.error("Failed to convert json to EntityLD:" + e.getLocalizedMessage());
////            }
////        }
////
////        return ret;
////    }
//
//        private JsonElement parseResponse(String response) throws Exception{
//
//            JsonElement result = new JsonObject();
//            try {
//                result = parser.parse(response);
//            }
//            catch (Exception e){
//                Exception exception = new Exception("Failed to parse an RPC response:" +e.getLocalizedMessage(), e.getCause());
//                LOGGER.error(exception.getLocalizedMessage());
//                exception.printStackTrace();
//                throw exception;
//            }
//
//            if(result instanceof JsonObject && ((JsonObject)result).has("error")) {
//                Object error = ((JsonObject)result).get("error");
//                String message = (error instanceof JsonObject? ((JsonObject)error).toString(): String.valueOf(error));
//                Exception exception = new Exception(message);
////            LOGGER.error(exception.getLocalizedMessage());
////            exception.printStackTrace();
//                throw exception;
//            }
//
//            return result;
//        }
//
//        @Override
//        public void close(){
//
//        }
//    }
//
//
//}
