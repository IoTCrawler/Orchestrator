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


import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.interfaces.IoTCrawlerClient;
import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.core.ontologies.SOSA;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.EntityInfo;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.google.gson.JsonParser;
import com.orange.ngsi2.model.*;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;



//ToDo: timeout for requests
//@RunWith(Parameterized.class)
public class IoTCrawlerRESTClient extends IoTCrawlerClient implements AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);
    private boolean cutURLs = true;

    //Semaphore callFinishedMutex;

    JsonParser parser;
    String orchestratorUrl;// = "http://localhost:3001/ngsi-ld/";
    NgsiLDClient client;
    GraphQLClient graphQLClient;
    //RabbitRpcClient rabbitRpcClient;
    //Consumer consumer;

    public IoTCrawlerRESTClient(String ngsildEndpoint, String graphQLEndpoint){

        this(ngsildEndpoint, graphQLEndpoint, false);
    }

    public IoTCrawlerRESTClient(String ngsildEndpointUrl, String graphQLEndpoint, Boolean cutURLs){
        LOGGER.info("Initializing IoTCrawlerRESTClient client to {}", ngsildEndpointUrl);
        orchestratorUrl = ngsildEndpointUrl;
        this.cutURLs = cutURLs;
        client = new NgsiLDClient(orchestratorUrl);
        graphQLClient = new GraphQLClient(graphQLEndpoint);
    }

    @Override
    public void init() throws Exception {

    }


    @Override
    public void run() throws Exception {

    }


    @Override
    public List<IoTStream> getStreams(Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = IoTStream.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
       Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
       List<EntityLD> entities = paginated.getItems();
       List<IoTStream> ret =  Utils.convertEntitiesToTargetClass(entities, IoTStream.class);
       return ret;
    }


    @Override
    public List<Sensor> getSensors(Map<String, Object> query, int offset, int limit) throws Exception {
        String type = Sensor.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<Sensor> ret =  Utils.convertEntitiesToTargetClass(entities, Sensor.class);
        return ret;
    }



    @Override
    public List<Platform> getPlatforms(Map<String, Object> query, int offset, int limit) throws Exception {
        String type = Platform.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<Platform> ret =  Utils.convertEntitiesToTargetClass(entities, Platform.class);
        return ret;
    }

    @Override
    public List<ObservableProperty> getObservableProperties(Map<String, Object> query, int offset, int limit) throws Exception {
        String type = ObservableProperty.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<ObservableProperty> ret =  Utils.convertEntitiesToTargetClass(entities, ObservableProperty.class);
        return ret;
    }


    @Override
    public List<String> getEntityURIs(Map<String, Object> query, int offset, int limit) {
        throw new NotImplementedException("Not implemented");
    }


    @Override
    public EntityLD getEntityById(String id) throws Exception {
        //String type = entityType;
//        if(cutURLs && type.startsWith("http://"))
//            type = Utils.cutURL(type, RDFModel.getNamespaces());
        EntityLD entity = client.getEntitySync( id  , null, null);
        return entity;
    }


//    @Override
//    public List<EntityLD> getEntities(String entityType, Map<String, Object> query, int offset, int limit) throws Exception {
//        GetEntitiesCommand command = new GetEntitiesCommand(entityType, query, offset, limit);
//        List<EntityLD> entities = execute(command);
//        return entities;
//    }

    @Override
    public <T> List<T> getEntities(Class<T> targetClass, Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = Utils.getTypeURI(targetClass);
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null, offset, limit, false).get();
        List<EntityLD> entities0 = paginated.getItems();

        List<T> entities = Utils.convertEntitiesToTargetClass(entities0, targetClass);
        return entities;
    }

    @Override
    public List<EntityLD> getEntities(String entityType, Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = entityType;
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null, offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();

        return entities;
    }


    @Override
    public String subscribeTo(String streamId, Function<StreamObservation, Void> onChange) throws Exception {
        String propertyId = null;
        try {
            propertyId = graphQLClient.getStreamObservationsByStreamId(streamId);
        }
        catch (Exception e){
            LOGGER.error("Failed to execute GraphQL Request: {}", e.getLocalizedMessage());
            throw new Exception("Failed to execute GraphQL Request");
        }
        if(propertyId==null)
            throw new Exception("Observable property for stream "+streamId+" not found");

        EntityInfo entityInfo = new EntityInfo(propertyId, StreamObservation.getTypeUri());
        //URL orchestratorURL =  new URL(System.getenv(IOTCRAWLER_ORCHESTRATOR_URL));
        //String referenceURL = "http://"+orchestratorURL.getHost()+":"+orchestratorURL.getPort()+"/notify";
        //String refURL = (endpointUrl!=null? endpointUrl:);

        //Endpoint endpoint = new Endpoint(new URL(referenceURL), ContentType.APPLICATION_JSON);

        NotificationParams notification = new NotificationParams();
        notification.setAttributes(Arrays.asList(new String[]{ SOSA.hasSimpleResult}));
        //notification.setEndpoint(null);

        String subscriptionId = UUID.randomUUID().toString();
        Subscription subscription = new Subscription(
                subscriptionId,
                Arrays.asList(new EntityInfo[]{ entityInfo }),
                Arrays.asList(new String[]{ SOSA.hasSimpleResult}),
                notification,
                null,
                null);


        subscribe(subscription, new Function<byte[], Void>() {
            @Override
            public Void apply(byte[] bytes) {
                StreamObservation streamObservation = null;
                try {
                    String jsonString = new String(bytes);
                    streamObservation = StreamObservation.fromJson(jsonString);
                }
                catch (Exception e){
                    LOGGER.error("Failed to create StreamObservation from Json");
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
        subscribe(subscription, onChange);
    }

    private String subscribe(Subscription subscription, Function<byte[], Void> onChange) throws Exception {

        Semaphore reqFinished = new Semaphore(0);
//        Semaphore deleteFinished = new Semaphore(0);

        List<Exception> exception = new ArrayList<>();
//        boolean updating = false;

        ListenableFuture<String> req = client.addSubscription(subscription);
        String[] result = new String[]{ subscription.getId() };
        req.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String queueName){
                result[0]=queueName;
                LOGGER.debug("Subscription {} registered", subscription.getId());
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Exception e = new Exception("Failed to create subscription "+ subscription.getId()+": "+ throwable.getLocalizedMessage(), throwable);
                exception.add(e);
                reqFinished.release();
            }

        });
        reqFinished.acquire();
        if(exception.size()>0) {
            Exception e = exception.get(0);
            LOGGER.error("Failed to register subscription: {}", e.getLocalizedMessage());
            throw e;
        }

        return result[0];
    }


//    @Override
//    public Boolean registerEntity(RDFModel rdfModel) throws Exception {
//        ListenableFuture<Void> ret = ngsiLDClient.addEntity(rdfModel.toEntityLD(true));
//        throw new NotImplementedException("");
//
//    }

//    @Override
//    public List<StreamObservation> getObservations(String streamId,int offset, int limit) throws Exception {
//        //GetObservationsCommand command = new GetObservationsCommand(streamId, offset, limit);
//        throw new NotImplementedException("");
//    }

//    @Override
//    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
//        PushObservationsCommand command = new PushObservationsCommand(observations);
//
//        LOGGER.info("Measuments pushed");
//        throw new NotImplementedException("");
//    }


    @Override
    public void close(){
       
    }
}
