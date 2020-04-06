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
import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.core.models.*;

import com.agtinternational.iotcrawler.fiware.clients.CustomSubscribeContextRequest;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.google.gson.JsonParser;
import com.orange.ngsi2.model.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.HttpClientErrorException;

import javax.management.InstanceAlreadyExistsException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;



//ToDo: timeout for requests
//@RunWith(Parameterized.class)
public class IoTCrawlerRESTClient extends IotCrawlerClient implements AutoCloseable {
    private Logger LOGGER = LoggerFactory.getLogger(IoTCrawlerRPCClient.class);
    private boolean cutURLs = true;

    //Semaphore callFinishedMutex;

    JsonParser parser;
    String orchestratorUrl = "http://localhost:3001/ngsi-ld/";
    NgsiLDClient client;
    //RabbitRpcClient rabbitRpcClient;
    //Consumer consumer;

    public IoTCrawlerRESTClient(String url){
        this(url, false);
    }

    public IoTCrawlerRESTClient(String url, Boolean cutURLs){
        LOGGER.info("Initializing IoTCrawlerRESTClient client to {}", url);
        orchestratorUrl = url;
        this.cutURLs = cutURLs;
        client = new NgsiLDClient(orchestratorUrl);
    }

    @Override
    public void init(){



    }


    @Override
    public void run() throws Exception {

    }


    @Override
    public List<IoTStream> getStreams(String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = IoTStream.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
       Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
       List<EntityLD> entities = paginated.getItems();
       List<IoTStream> ret =  Utils.convertEntitiesToTargetClass(entities, IoTStream.class);
       return ret;
    }


    @Override
    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception {
        String type = Sensor.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<Sensor> ret =  Utils.convertEntitiesToTargetClass(entities, Sensor.class);
        return ret;
    }



    @Override
    public List<Platform> getPlatforms(String query, int offset, int limit) throws Exception {
        String type = Platform.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<Platform> ret =  Utils.convertEntitiesToTargetClass(entities, Platform.class);
        return ret;
    }

    @Override
    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception {
        String type = ObservableProperty.getTypeUri();
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null,  offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();
        List<ObservableProperty> ret =  Utils.convertEntitiesToTargetClass(entities, ObservableProperty.class);
        return ret;
    }


    @Override
    public List<String> getEntityURIs(String query, int offset, int limit) {
        throw new NotImplementedException("Not implemented");
    }


    @Override
    public List<EntityLD> getEntitiesById(String[] ids) throws Exception {
        //String type = entityType;
//        if(cutURLs && type.startsWith("http://"))
//            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(Arrays.asList(ids), null, null,  null, null,null, null, 0, 0, false).get();
        List<EntityLD> entities = paginated.getItems();

        return entities;
    }


//    @Override
//    public List<EntityLD> getEntities(String entityType, String query, int offset, int limit) throws Exception {
//        GetEntitiesCommand command = new GetEntitiesCommand(entityType, query, offset, limit);
//        List<EntityLD> entities = execute(command);
//        return entities;
//    }

    @Override
    public <T> List<T> getEntities(Class<T> targetClass, String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = Utils.getTypeURI(targetClass);
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null, offset, limit, false).get();
        List<EntityLD> entities0 = paginated.getItems();

        List<T> entities = Utils.convertEntitiesToTargetClass(entities0, targetClass);
        return entities;
    }

    @Override
    public List<EntityLD> getEntities(String entityType, String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        String type = entityType;
        if(cutURLs && type.startsWith("http://"))
            type = Utils.cutURL(type, RDFModel.getNamespaces());
        Paginated<EntityLD> paginated = client.getEntities(null, null, Arrays.asList(type),  null, query,null, null, offset, limit, false).get();
        List<EntityLD> entities = paginated.getItems();

        return entities;
    }

//    @Override
//    public Boolean registerEntity(RDFModel rdfModel) throws Exception {
//        ListenableFuture<Void> ret = ngsiLDClient.addEntity(rdfModel.toEntityLD(true));
//        throw new NotImplementedException("");
//
//    }

    @Override
    public List<StreamObservation> getObservations(String streamId,int offset, int limit) throws Exception {
        //GetObservationsCommand command = new GetObservationsCommand(streamId, offset, limit);
        throw new NotImplementedException("");
    }

//    @Override
//    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception {
//        PushObservationsCommand command = new PushObservationsCommand(observations);
//
//        LOGGER.info("Measuments pushed");
//        throw new NotImplementedException("");
//    }

    @Override
    public String subscribeTo(Subscription subscription, Function<StreamObservation, Void> onChange) throws Exception {


        Semaphore reqFinished = new Semaphore(0);
//        Semaphore deleteFinished = new Semaphore(0);
        List<String> ret = new ArrayList<>();
        List<Exception> exception = new ArrayList<>();
//        boolean updating = false;
        try {

            ListenableFuture<String> req;

            req = client.addSubscription(subscription);
            req.addCallback(new ListenableFutureCallback<String>() {
                @Override
                public void onSuccess(String subcriptionId) {
                    LOGGER.debug("Subscription registered {}");
                    ret.add(subcriptionId);
                    reqFinished.release();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Exception e = new Exception("Failed to register subscription "+ subscription.getId()+": "+ throwable.getLocalizedMessage());
                    exception.add(e);
                    reqFinished.release();
                }

            });
            reqFinished.acquire();
        }
        catch (Exception e){
            LOGGER.error("Failed to register subscription: {}", e.getLocalizedMessage());
            e.printStackTrace();
            throw e;
        }
        return (ret.size()>0?ret.get(0):null);

//        client.addSubscription();
//        CustomSubscribeContextRequest request = new CustomSubscribeContextRequest();
//        request.setReference(referenceUrl);
//        //request.setEntityIdList(Arrays.asList(contextElement.getEntityId()));
//        request.setEntityIdList(Arrays.asList(new EntityId(entityId, URI.create(StreamObservation.getTypeUri()), false)));
//        //request.setAttributeList(Arrays.asList(new String[]{ "noiseLevel" }));
//        //request.setEntityIdList(new EntityId(){{  setId(id); setIsPattern(true); }} );
//        request.setAttributeList(Arrays.asList(attributes));
//
//        if(restriction==null)
//            restriction = new Restriction();
//        //restriction.setAttributeExpression();
//        request.setRestriction(restriction);
//
//        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
//        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONTIMEINTERVAL, Arrays.asList(new String[]{ "noiseLevel" }), null);
//
//        javax.xml.datatype.Duration duration = null;
//        try {
//            duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
//            request.setDuration(duration);
//        } catch (DatatypeConfigurationException e) {
//            e.printStackTrace();
//        }
//        request.setNotifyCondition(notifyConditions);
//
//        SubscribeContextResponse response = client.addSubscription().subscribeContext(request, URI.create(ioTBrokerEndpoint));
//        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null && response.getSubscribeError().getStatusCode().getCode()!=200) {
//            LOGGER.error(response.getSubscribeError().getStatusCode().toJsonString());
//            throw new Exception(response.getSubscribeError().getStatusCode().toJsonString());
//        }
//
//        String subscriptionId = response.getSubscribeResponse().getSubscriptionId();
//        return subscriptionId;
    }



    @Override
    public void close(){
       
    }
}
