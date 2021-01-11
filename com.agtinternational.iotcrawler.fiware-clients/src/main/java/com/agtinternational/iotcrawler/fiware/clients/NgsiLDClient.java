package com.agtinternational.iotcrawler.fiware.clients;

/*-
 * #%L
 * fiware-clients
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


import com.agtinternational.iotcrawler.fiware.models.EntityLD;

import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orange.ngsi2.model.*;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.*;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.concurrent.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.*;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.valueOf;

/**
 * NGSIv2 API Client
 */
public class NgsiLDClient {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static Map<String, ?> noParams = Collections.emptyMap();

    private AsyncRestTemplate asyncRestTemplate;

    private HttpHeaders httpHeaders;

    private String baseURL;

    private NgsiLDClient() {
        LOGGER.info("This product includes software developed by NEC Europe Ltd");
        // set default headers for Content-Type and Accept to application/JSON
        httpHeaders = new HttpHeaders();
        //Both headers required for DJANE broker
        //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));
        //httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setAccept(Collections.singletonList(valueOf("*/*")));  //nec broker ignores the context

//        TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//                    public void checkClientTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//                    public void checkServerTrusted(
//                            java.security.cert.X509Certificate[] certs, String authType) {
//                    }
//                }
//        };
//
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new SecureRandom());
//        }
//        catch (Exception e){
//            LOGGER.error("Failed to init SSL Context: {}", e.getLocalizedMessage());
//        }
//        if(sc!=null) {
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HostnameVerifier hv = new HostnameVerifier() {
//                public boolean verify(String urlHostName, SSLSession session) {
//                    if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
//                        System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
//                    }
//                    return true;
//                }
//            };
//            HttpsURLConnection.setDefaultHostnameVerifier(hv);
//        }
    }

//    /**
//     * Default constructor
//     * @param asyncRestTemplate CustomAsyncRestTemplate to handle requests
//     * @param baseURL base URL for the NGSIv2 service
//     */
//    public NgsiLDClient(AsyncRestTemplate asyncRestTemplate, String baseURL) {
//        this();
//        this.asyncRestTemplate = asyncRestTemplate;
//        this.baseURL = baseURL;
//
//        // Inject NGSI2 error handler and Java 8 support
//        injectNgsi2ErrorHandler();
//        injectJava8ObjectMapper();
//    }

    public NgsiLDClient(String baseURL){
        this();

        this.baseURL = baseURL;
        //AsyncClientHttpRequestFactory requestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        //This client allows to ignore certificate exceptions, but fails to init in ansestor code
        //AsyncClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        this.asyncRestTemplate = new CustomAsyncRestTemplate(requestFactory, new NgsiLDRestTemplate());
        //this(, baseURL);
    }

    /**
     * @return the list of supported operations under /v2
     */
    public ListenableFuture<Map<String, String>> getV2() {
        ListenableFuture<ResponseEntity<JsonNode>> responseFuture = request(HttpMethod.GET, baseURL + "v2", null, JsonNode.class);
        return new ListenableFutureAdapter<Map<String, String>, ResponseEntity<JsonNode>>(responseFuture) {
            @Override
            protected Map<String, String> adapt(ResponseEntity<JsonNode> result) throws ExecutionException {
                Map<String, String> services = new HashMap<>();
                result.getBody().fields().forEachRemaining(entry -> services.put(entry.getKey(), entry.getValue().textValue()));
                return services;
            }
        };
    }

    public boolean addEntitySync(EntityLD entity) throws Exception {
        final Boolean[] success = {false};
        ListenableFuture<Void> req = addEntity(entity);
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                errors.add(new Exception(throwable));
                reqFinished.release();
            }

        });
        try {
            req.get();
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        if(!errors.isEmpty())
            throw errors.get(0);

        return success[0];
    }

    //public EntityLD getEntitySync(String entityId, String type, Collection<String> attrs) throws Exception {
    public EntityLD getEntitySync(String entityId, String type, Collection<String> attrs) throws Exception {

        ListenableFuture<EntityLD> req = getEntity(entityId, type, attrs);
        Semaphore reqFinished = new Semaphore(0);
        final List<EntityLD> ret = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new SuccessCallback<EntityLD>() {
            @Override
            public void onSuccess(EntityLD entityLD) {
                ret.add(entityLD);
                reqFinished.release();
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                if(throwable instanceof HttpClientErrorException.NotFound ||
                        (throwable.getCause()!=null
                                && throwable.getCause().getCause() instanceof HttpClientErrorException
                                && ((HttpClientErrorException)throwable.getCause().getCause()).getStatusCode()==HttpStatus.NOT_FOUND
                        )
                )
                    LOGGER.debug("Entity {} not found", entityId);
                else {
                    if (throwable instanceof Exception)
                        errors.add((Exception) throwable);
                    else
                        errors.add(new Exception(throwable));
                }
                reqFinished.release();
            }
        });

        try {
            req.get();
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        if(!errors.isEmpty())
            throw errors.get(0);

        if(ret.size()>0)
            return ret.get(0);
        return null;
    }

    public boolean updateEntitySync(EntityLD entityLD, boolean append) throws Exception {
        ListenableFuture<Void> req = updateEntity(entityLD, append);
        final Boolean[] success = {false};
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                errors.add(new Exception(throwable));
                reqFinished.release();
            }

        });

        try {
            req.get();
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        if(!errors.isEmpty())
            throw errors.get(0);

        return success[0];

    }

    public boolean updateEntityWithDeletionSync(EntityLD entityLD, boolean append) throws Exception {
        ListenableFuture<Void> req = updateEntityWithDeletion(entityLD, append);
        final Boolean[] success = {false};
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                errors.add(new Exception(throwable));
                reqFinished.release();
            }

        });

        try {
            req.get();
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        if(!errors.isEmpty())
            throw errors.get(0);

        return success[0];

    }

    public boolean deleteEntitySync(String entityId) throws Exception {
        final Boolean[] success = {false};
        ListenableFuture<Void> req = deleteEntity(entityId);
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                errors.add(new Exception(throwable));
                reqFinished.release();
            }

        });
        try {
            req.get();
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        if(!errors.isEmpty())
            throw errors.get(0);
        return success[0];
    }



    /*
     * Entities requests
     */

    /**
     * Retrieve a list of Entities (simplified)
     * @param ids an optional list of entity IDs (cannot be used with idPatterns)
     * @param idPattern an optional pattern of entity IDs (cannot be used with ids)
     * @param types an optional list of types of entity
     * @param attrs an optional list of attributes to return for all entities
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a pagined list of Entities
     */
    public ListenableFuture<Paginated<EntityLD>> getEntities(Collection<String> ids, String idPattern,
                                                           Collection<String> types, Collection<String> attrs,
                                                           int offset, int limit, boolean count) throws Exception {

        return getEntities(ids, idPattern, types, attrs, null, null, null, offset, limit, count);
    }

    public Paginated<EntityLD> getEntitiesSync(Collection<String> ids, String idPattern,
                                               Collection<String> types, Collection<String> attrs,
                                               Map<String,Object> query, GeoQuery geoQuery,
                                               Collection<String> orderBy,
                                               int offset, int limit, boolean count) throws Exception {


        ListenableFuture<Paginated<EntityLD>> req = getEntities(ids, idPattern, types, attrs, query, geoQuery, orderBy, offset, limit, count);
        Semaphore reqFinished = new Semaphore(0);
        final List<Paginated<EntityLD>> ret = new ArrayList<>();

        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<Paginated<EntityLD>>() {

            @Override
            public void onSuccess(Paginated<EntityLD> entityLDPaginated) {

                ret.add(entityLDPaginated);
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Throwable cause = throwable.getCause().getCause();
                if(cause instanceof HttpClientErrorException && ((HttpClientErrorException)cause).getStatusCode().value()==404){
                    LOGGER.debug("Entity not found");
                }else{
                    LOGGER.error("Failed to get entities: {}", throwable.getLocalizedMessage());
                    errors.add(new Exception(throwable));
                }
                reqFinished.release();
            }
        });

        try {
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        if(errors.size()>0)
            throw errors.get(0);

        if (ret.size()>0)
            return ret.get(0);

        return new Paginated<>(new ArrayList<EntityLD>(), 0,0,0);
    }

    /**
     * Retrieve a list of Entities
     * @param ids an optional list of entity IDs (cannot be used with idPatterns)
     * @param idPattern an optional pattern of entity IDs (cannot be used with ids)
     * @param types an optional list of types of entity
     * @param attrs an optional list of attributes to return for all entities
     * @param query an optional Simple Query Language query
     * @param geoQuery an optional Geo query
     * @param orderBy an option list of attributes to difine the order of entities
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a pagined list of Entities
     */
    public ListenableFuture<Paginated<EntityLD>> getEntities(Collection<String> ids, String idPattern,
                                                             Collection<String> types, Collection<String> attrs,
                                                             Map<String,Object> query, GeoQuery geoQuery,
                                                             Collection<String> orderBy,
                                                             int offset, int limit, boolean count) throws Exception {

//        List<String> encodedIds = null;
//        if(ids!=null) {
//            encodedIds = new ArrayList<>();
//            Iterator<String> iterator = ids.iterator();
//            while (iterator.hasNext()) {
//                String id = iterator.next();
//                //if (type.startsWith("http://"))
//                    id = URLEncoder.encode(id);
//                encodedIds.add(id);
//            }
//        }

        List<String> encodedTypes = null;
        if(types!=null) {
            encodedTypes = new ArrayList<>();
            Iterator<String> iterator = types.iterator();
            while (iterator.hasNext()) {
                String type = iterator.next();
                //if (type.startsWith("http://"))
                    //type = type.replace("#", "%23");

                //A skipping types encoding because faceted filtering would not work
                //type = URLEncoder.encode(type);
                //Not encoded types should work, e.g.: http://192.168.0.125:9090/ngsi-ld/v1/entities?type=http://www.w3.org/ns/sosa/Platform
                encodedTypes.add(type);
            }
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);

        if(ids!=null)
            builder.path("v1/entities/"+ids.toArray()[0]);
        else {
            builder.path("v1/entities");
            //builder.query(query);
            if(query!=null) {

                //StringBuilder q = new StringBuilder();
                List<String> conditions = new ArrayList<>();
                for (String key: query.keySet()){
                    Object value = query.get(key);
                    if(value instanceof String) {
                        String value1 = (String)value;
                        //value1 = value1.replace("#", "%23");
//                        value1 = URLEncoder.encode(value1);
//                        value1 = URLEncoder.encode(value1);
//                        value1 = value1.replace("%25","%");
//                        value1 = value1.replace("%3D","=");
//                        value1 = value1.replace("%22","\"");

                        String key1 = key;
                        //key1 = key1.replace("#", "%23");
//                        key1 = key1.replace("%25","%");
//                        key1 = key1.replace("%3D","=");
//                        key1 = key1.replace("%22","\"");



                        conditions.add(key1 + "==" + value1 + "");
                        //String query = "q=brandName==\"Mercedes\"";  //Scorpio
                        //String query = "brandName.value=Mercedes";   //djane
                    }else if(value instanceof Number) {
                        conditions.add(key +"=="+ value);
                    }else if(value instanceof Pair) {
                        Pair pair = (Pair)value;
                        conditions.add(key + pair.getKey()+pair.getValue());
                    }else
                        throw new Exception("Query map allows only string values for now!");
                }

                addParam(builder, "q", String.join(";", conditions));
            }

            addParam(builder, "id", ids);
            addParam(builder, "idPattern", idPattern);
            addParam(builder, "type", encodedTypes);
            addParam(builder, "attrs", attrs);
            //addParam(builder, "", query);  //see below

            addParam(builder, "attrs", attrs);
            addGeoQueryParams(builder, geoQuery);
            addParam(builder, "orderBy", orderBy);
            addPaginationParams(builder, offset, limit);
            if (count) {
                addParam(builder, "options", "count");
            }
        }

        //String url = builder.build().encode().toUriString(); //djane requires encoded IDs
        String url = builder.build().toUriString();

        //these symbols would be encorded again in the AsyncHttpRequest, so need decode them back

//        url = url.replace("%253D","=");
        url = url.replace("%25","%");
        url = url.replace("%3D","=");
        url = url.replace("%22","\"");

//        url = url.replace("%255B","%5B");
//        url = url.replace("%255D","%5D");

        LOGGER.trace("Requesting {}", url);
        return adaptPaginated(request(HttpMethod.GET, url, null, EntityLD[].class), offset, limit);
    }

    /**
     * Create a new entity
     * @param entity the Entity to add
     * @return the listener to notify of completion
     */
    public ListenableFuture<Void> addEntity(EntityLD entity){

        HttpHeaders httpHeaders = cloneHttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));

        ListenableFuture<Void> ret = adapt(request(HttpMethod.POST, UriComponentsBuilder.fromHttpUrl(baseURL).path("v1/entities/").toUriString(), httpHeaders, entity,  Void.class));
        //Additional logic getting added entity back
        List<Exception> exceptions = new ArrayList<>();
        ret.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                exceptions.add(new Exception(throwable));
            }

            @Override
            public void onSuccess(Void aVoid) {
                LOGGER.debug("Entity is expected to be added. Trying to get  back");
                ListenableFuture<EntityLD> ret2 = getEntity(entity.getId(),null,null);
                ret2.addCallback(new ListenableFutureCallback<EntityLD>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        exceptions.add(new Exception("Failed to get added back", throwable));
                        throwable.printStackTrace();
                        ret.cancel(true);
                    }

                    @Override
                    public void onSuccess(EntityLD entityLD) {

                    }
                });
            }
        });

        return ret;
    }

    /**
     * Get an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attrs the list of attributes to retreive for this entity, null or empty means all attributes
     * @return the entity
     */
    public ListenableFuture<EntityLD> getEntity(String entityId, String type, Collection<String> attrs) {
        HttpHeaders httpHeaders = cloneHttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}");
        addParam(builder, "type", type);
        addParam(builder, "attrs", attrs);
        ListenableFuture<EntityLD> ret = adapt(request(HttpMethod.GET, builder.buildAndExpand(entityId).toUriString(), httpHeaders, null, EntityLD.class));
        return ret;
    }


    public ListenableFuture<Void> updateEntity(EntityLD entity, boolean append){

        HttpHeaders httpHeaders = cloneHttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs");
        //addParam(builder, "type", type);
        if (append) {
            addParam(builder, "options", "append");
        }
        Map attributes = entity.getAttributes();
        attributes.put("@context", entity.getContext());

        //Patch is not really updating. Only POST
        ListenableFuture<Void> ret = adapt(request(HttpMethod.POST, builder.buildAndExpand(entity.getId()).toUriString(), httpHeaders, attributes, Void.class));
        return ret;
    }

    public ListenableFuture<Void> updateEntityWithDeletion(EntityLD entity, boolean append){
            return new ListenableFuture<Void>() {

            List<ListenableFutureCallback<? super Void>> callbacks = new ArrayList<>();

            ListenableFuture<Void> voidFuture = null;

            @Override
            public void addCallback(ListenableFutureCallback<? super Void> listenableFutureCallback) {
                callbacks.add(listenableFutureCallback);
            }

            @Override
            public void addCallback(SuccessCallback<? super Void> successCallback, FailureCallback failureCallback) {
                throw new NotImplementedException("");
            }

            @Override
            public boolean cancel(boolean b) {
                return voidFuture.cancel(b);
            }

            @Override
            public boolean isCancelled() {
                return voidFuture.isCancelled();
            }

            @Override
            public boolean isDone() {
                return voidFuture.isDone();
            }

            @Override
            public Void get() throws InterruptedException, ExecutionException {
                ListenableFuture<EntityLD> getEntityRequest = getEntity(entity.getId(), entity.getType(), null);
                getEntityRequest.addCallback(new ListenableFutureCallback<EntityLD>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LOGGER.error("Failed to get entity during update procedure: ", throwable.getLocalizedMessage());
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(EntityLD oldEntityLD) {

                        //List<Exception> errors = new ArrayList<>();
                        Boolean delete=false;
                        for(String key: oldEntityLD.getAttributes().keySet()){
                            if(!entity.getAttributes().containsKey(key))
                                delete=true;
                        }

                        if(delete) {
                            ListenableFuture<Void> deleteRequest = deleteEntity(entity.getId());
                            deleteRequest.addCallback(new ListenableFutureCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    try {
                                        voidFuture = addEntity(entity);
                                        for(ListenableFutureCallback<? super Void> callback: callbacks)
                                            voidFuture.addCallback(callback);
                                        voidFuture.get();
                                    } catch (Exception e) {
                                        LOGGER.error("Failed to add entity during updating it:", e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    LOGGER.error("Failed to delete entity during update procedure:", throwable.getLocalizedMessage());
                                    throwable.printStackTrace();
                                }
                            });
                            voidFuture = deleteRequest;
                        }else
                            voidFuture = updateEntity(entity, true);


                        for(ListenableFutureCallback<? super Void> callback: callbacks)
                            voidFuture.addCallback(callback);

                        try {
                            voidFuture.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                getEntityRequest.get();
                return null;
            }

            @Override
            public Void get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return get();
            }
        };
    }


    /**
     * Replace all the existing attributes of an entity with a new set of attributes
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributes the new set of attributes
     * @return the listener to notify of completion
     */
    public ListenableFuture<Void> replaceEntity(String entityId, String type, Map<String, Attribute> attributes) {

        HttpHeaders httpHeaders = cloneHttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}");
        addParam(builder, "type", type);
        return adapt(request(HttpMethod.PUT, builder.buildAndExpand(entityId).toUriString(), httpHeaders, attributes, Void.class));
    }


    public ListenableFuture<Void> deleteEntity(String entityId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}");
        //addParam(builder, "type", type);
        return adapt(request(HttpMethod.DELETE, builder.buildAndExpand(entityId).toUriString(), null, Void.class));
    }

    /*
     * Attributes requests
     */

    /**
     * Retrieve the attribute of an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributeName the attribute name
     * @return
     */
    public ListenableFuture<Attribute> getAttribute(String entityId, String type, String attributeName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs/{attributeName}");
        addParam(builder, "type", type);
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(entityId, attributeName).toUriString(), null, Attribute.class));
    }

    /**
     * Update the attribute of an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributeName the attribute name
     * @return
     */
    public ListenableFuture<Void> updateAttribute(String entityId, String type, String attributeName, Attribute attribute) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs/{attributeName}");
        addParam(builder, "type", type);
        return adapt(request(HttpMethod.PUT, builder.buildAndExpand(entityId, attributeName).toUriString(), attribute, Void.class));
    }

    /**
     * Delete the attribute of an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributeName the attribute name
     * @return
     */
    public ListenableFuture<Attribute> deleteAttribute(String entityId, String type, String attributeName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs/{attributeName}");
        addParam(builder, "type", type);
        return adapt(request(HttpMethod.DELETE, builder.buildAndExpand(entityId, attributeName).toUriString(), null, Attribute.class));
    }

    public Attribute deleteAttributeSync(String entityId, String type, String attributeName) throws Exception {
        ListenableFuture<Attribute> req = deleteAttribute(entityId, type, attributeName);
        List<Attribute> ret = new ArrayList<>();
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();

        req.addCallback(new SuccessCallback<Attribute>() {
            @Override
            public void onSuccess(Attribute attribute) {
                ret.add(attribute);
                reqFinished.release();
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                errors.add(new Exception(throwable));
                reqFinished.release();
            }
        });

        reqFinished.acquire();

        if(!errors.isEmpty())
            throw errors.get(0);
        if(ret.size()>0)
            return ret.get(0);
        return null;
    }

    /*
     * Attribute values requests
     */

    /**
     * Retrieve the attribute of an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributeName the attribute name
     * @return
     */
    public ListenableFuture<Object> getAttributeValue(String entityId, String type, String attributeName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs/{attributeName}/value");
        addParam(builder, "type", type);
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(entityId, attributeName).toUriString(), null, Object.class));
    }

    /**
     * Retrieve the attribute of an entity
     * @param entityId the entity ID
     * @param type optional entity type to avoid ambiguity when multiple entities have the same ID, null or zero-length for empty
     * @param attributeName the attribute name
     * @return
     */
    public ListenableFuture<String> getAttributeValueAsString(String entityId, String type, String attributeName) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/entities/{entityId}/attrs/{attributeName}/value");
        addParam(builder, "type", type);
        HttpHeaders httpHeaders = cloneHttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(entityId, attributeName).toUriString(), httpHeaders, null, String.class));
    }

    /*
     * Entity Type requests
     */

    /**
     * Retrieve a list of entity types
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a pagined list of entity types
     */
    public ListenableFuture<Paginated<EntityType>> getEntityTypes(int offset, int limit, boolean count) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/types");
        addPaginationParams(builder, offset, limit);
        if (count) {
            addParam(builder, "options", "count");
        }
        return adaptPaginated(request(HttpMethod.GET, builder.toUriString(), null, EntityType[].class), offset, limit);
    }

    /**
     * Retrieve an entity type
     * @param entityType the entityType to retrieve
     * @return an entity type
     */
    public ListenableFuture<EntityType> getEntityType(String entityType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/types/{entityType}");
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(entityType).toUriString(), null, EntityType.class));
    }

    /*
     * Registrations requests
     */

    /**
     * Retrieve the list of all Registrations
     * @return a list of registrations
     */
    public ListenableFuture<List<Registration>> getRegistrations() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/registrations");

        ListenableFuture<ResponseEntity<Registration[]>> e = request(HttpMethod.GET, builder.toUriString(), null, Registration[].class);
        return new ListenableFutureAdapter<List<Registration>, ResponseEntity<Registration[]>>(e) {
            @Override
            protected List<Registration> adapt(ResponseEntity<Registration[]> result) throws ExecutionException {
                return new ArrayList<>(Arrays.asList(result.getBody()));
            }
        };
    }

    /**
     * Create a new registration
     * @param registration the Registration to add
     * @return the listener to notify of completion
     */
    public ListenableFuture<Void> addRegistration(Registration registration) {
        return adapt(request(HttpMethod.POST, UriComponentsBuilder.fromHttpUrl(baseURL).path("v1/registrations").toUriString(), registration, Void.class));
    }

    /**
     * Retrieve the registration by registration ID
     * @param registrationId the registration ID
     * @return registration
     */
    public ListenableFuture<Registration> getRegistration(String registrationId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/registrations/{registrationId}");
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(registrationId).toUriString(), null, Registration.class));
    }

    /**
     * Update the registration by registration ID
     * @param registrationId the registration ID
     * @return
     */
    public ListenableFuture<Void> updateRegistration(String registrationId, Registration registration) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/registrations/{registrationId}");
        return adapt(request(HttpMethod.PATCH, builder.buildAndExpand(registrationId).toUriString(), registration, Void.class));
    }

    /**
     * Delete the registration by registration ID
     * @param registrationId the registration ID
     * @return
     */
    public ListenableFuture<Void> deleteRegistration(String registrationId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/registrations/{registrationId}");
        return adapt(request(HttpMethod.DELETE, builder.buildAndExpand(registrationId).toUriString(), null, Void.class));
    }

    /*
     * Subscriptions requests
     */

    /**
     * Retrieve the list of all Subscriptions present in the system
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a pagined list of Subscriptions
     */
    public ListenableFuture<Paginated<Subscription>> getSubscriptions(int offset, int limit, boolean count) {

//        HttpHeaders httpHeaders = cloneHttpHeaders();
//        httpHeaders.setContentType(MediaType.valueOf("application/ld+json"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/subscriptions/");
        addPaginationParams(builder, offset, limit);
        if (count) {
            addParam(builder, "options", "count");
        }

        return adaptPaginated(request(HttpMethod.GET, builder.toUriString(), httpHeaders, null, Subscription[].class), offset, limit);
    }

    public Paginated<Subscription> getSubscriptionsSync(int offset, int limit, boolean count) throws Exception {
        ListenableFuture<Paginated<Subscription>> req = getSubscriptions(offset,limit,count);
        List<Subscription> ret = new ArrayList<>();
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();

        req.addCallback(new SuccessCallback<Paginated<Subscription>>() {
                            @Override
                            public void onSuccess(Paginated<Subscription> subscriptionPaginated) {
                                ret.addAll(subscriptionPaginated.getItems());
                                reqFinished.release();
                            }
                        },
                new FailureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        errors.add(new Exception(throwable));
                        reqFinished.release();
                    }
                });
        reqFinished.acquire();

        if(!errors.isEmpty())
            throw errors.get(0);
        return new Paginated<>(ret, offset, limit, ret.size());
    }

    /**
     * Create a new subscription
     * @param subscription the Subscription to add
     * @return subscription Id
     */
    public ListenableFuture<String> addSubscription(Subscription subscription) {
        ListenableFuture<ResponseEntity<String>> s = request(HttpMethod.POST, UriComponentsBuilder.fromHttpUrl(baseURL).path("v1/subscriptions").toUriString(), subscription, String.class);
        return new ListenableFutureAdapter<String, ResponseEntity<String>>(s) {
            @Override
            protected String adapt(ResponseEntity<String> result) throws ExecutionException {
                if(result.getStatusCode().isError())
                    throw new ExecutionException("Error "+result.getStatusCode().value(), new Throwable(result.getStatusCode().getReasonPhrase()));
                return result.getBody();
            }
        };
    }

    public String addSubscriptionSync(Subscription subscription) throws Exception {
        Semaphore reqFinished = new Semaphore(0);
        ListenableFuture<String> req = addSubscription(subscription);
        //final ResponseEntity<Void>[] ret = new ResponseEntity[]{ null };
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //ret[0] = result;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Failed to add subscription");
                errors.add(new Exception(throwable));
                reqFinished.release();
            }

        });
        reqFinished.acquire();
        
        if (errors.size()>0)
            throw errors.get(0);

        return subscription.getId();
    }



    /**
     * Get a Subscription by subscription ID
     * @param subscriptionId the subscription ID
     * @return the subscription
     */
    public ListenableFuture<Subscription> getSubscription(String subscriptionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/subscriptions/{subscriptionId}/");
        return adapt(request(HttpMethod.GET, builder.buildAndExpand(subscriptionId).toUriString(), null, Subscription.class));
    }

    /**
     * Update the subscription by subscription ID
     * @param subscriptionId the subscription ID
     * @return
     */
    public ListenableFuture<Void> updateSubscription(String subscriptionId, Subscription subscription) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/subscriptions/{subscriptionId}");
        return adapt(request(HttpMethod.PATCH, builder.buildAndExpand(subscriptionId).toUriString(), subscription, Void.class));
    }

    /**
     * Delete the subscription by subscription ID
     * @param subscriptionId the subscription ID
     * @return
     */
    public ListenableFuture<Void> deleteSubscription(String subscriptionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/subscriptions/{subscriptionId}/");
        return adapt(request(HttpMethod.DELETE, builder.buildAndExpand(subscriptionId).toUriString(), null, Void.class));
    }

    public void deleteSubscriptionSync(String subscriptionId) throws Exception {
        ListenableFuture<Void> req = deleteSubscription(subscriptionId);
        Semaphore reqFinished = new Semaphore(0);
        List<Exception> errors = new ArrayList<>();
        req.addCallback(new SuccessCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reqFinished.release();
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                errors.add(new Exception(throwable));
                reqFinished.release();
            }
        });
        reqFinished.acquire();

        if(!errors.isEmpty())
            throw errors.get(0);
    }

    /*
     * POJ RPC "bulk" Operations
     */

    /**
     * Update, append or delete multiple entities in a single operation
     * @param bulkUpdateRequest a BulkUpdateRequest with an actionType and a list of entities to update
     * @return Nothing on success
     */
    public ListenableFuture<Void> bulkUpdate(BulkUpdateRequest bulkUpdateRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/op/update");
        return adapt(request(HttpMethod.POST, builder.toUriString(), bulkUpdateRequest, Void.class));
    }

    /**
     * Query multiple entities in a single operation
     * @param bulkQueryRequest defines the list of entities, attributes and scopes to match entities
     * @param orderBy an optional list of attributes to order the entities (null or empty for none)
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a paginated list of entities
     */
    public ListenableFuture<Paginated<Entity>> bulkQuery(BulkQueryRequest bulkQueryRequest, Collection<String> orderBy, int offset, int limit, boolean count) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/op/query");
        addPaginationParams(builder, offset, limit);
        addParam(builder, "orderBy", orderBy);
        if (count) {
            addParam(builder, "options", "count");
        }
        return adaptPaginated(request(HttpMethod.POST, builder.toUriString(), bulkQueryRequest, Entity[].class), offset, limit);
    }

    /**
     * Create, update or delete registrations to multiple entities in a single operation
     * @param bulkRegisterRequest defines the list of entities to register
     * @return a list of registration ids
     */
    public ListenableFuture<String[]> bulkRegister(BulkRegisterRequest bulkRegisterRequest) {
        return adapt(request(HttpMethod.POST, UriComponentsBuilder.fromHttpUrl(baseURL).path("v1/op/register").toUriString(), bulkRegisterRequest, String[].class));
    }

    /**
     * Discover registration matching entities and their attributes
     * @param bulkQueryRequest defines the list of entities, attributes and scopes to match registrations
     * @param offset an optional offset (0 for none)
     * @param limit an optional limit (0 for none)
     * @param count true to return the total number of matching entities
     * @return a paginated list of registration
     */
    public ListenableFuture<Paginated<Registration>> bulkDiscover(BulkQueryRequest bulkQueryRequest, int offset, int limit, boolean count) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL);
        builder.path("v1/op/discover");
        addPaginationParams(builder, offset, limit);
        if (count) {
            addParam(builder, "options", "count");
        }
        return adaptPaginated(request(HttpMethod.POST, builder.toUriString(), bulkQueryRequest, Registration[].class), offset, limit);
    }

    /**
     * Default headers
     * @return the default headers
     */
    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * Make an HTTP request with default headers
     */
    protected <T,U> ListenableFuture<ResponseEntity<T>> request(HttpMethod method, String uri, U body, Class<T> responseType) {
        return request(method, uri, getHttpHeaders(), body, responseType);
    }

    /**
     * Make an HTTP request with custom headers
     */
    protected <T,U> ListenableFuture<ResponseEntity<T>> request(HttpMethod method, String uri, HttpHeaders httpHeaders, U body, Class<T> responseType) {
        HttpEntity<U> requestEntity = new HttpEntity<>(body, httpHeaders);
        return asyncRestTemplate.exchange(uri, method, requestEntity, responseType);
    }

    private <T> ListenableFuture<T> adapt(ListenableFuture<ResponseEntity<T>> responseEntityListenableFuture) {
        return new ListenableFutureAdapter<T, ResponseEntity<T>>(responseEntityListenableFuture) {
            @Override
            protected T adapt(ResponseEntity<T> result) throws ExecutionException {
                return result.getBody();
            }
        };
    }

    private <T> ListenableFuture<Paginated<T>> adaptPaginated(ListenableFuture<ResponseEntity<T[]>> responseEntityListenableFuture, int offset, int limit) {

        ListenableFutureAdapter<Paginated<T>, ResponseEntity<T[]>> ret = new ListenableFutureAdapter<Paginated<T>, ResponseEntity<T[]>>(responseEntityListenableFuture) {
            @Override
            protected Paginated<T> adapt(ResponseEntity<T[]> result){
                List<T> list = Arrays.asList(result.getBody());
                Paginated<T>  ret = new Paginated<T>(list, offset, limit, extractTotalCount(result));
                return ret;
            }
        };
        return ret;
    }

    private void addPaginationParams(UriComponentsBuilder builder, int offset, int limit) {
        if (offset > 0) {
            builder.queryParam("offset", offset);
        }
        if (limit > 0) {
            builder.queryParam("limit", limit);
        }
    }

    private void addParam(UriComponentsBuilder builder, String key, String value) {
        if (!nullOrEmpty(value)) {
            builder.queryParam(key, value);
        }
    }

    private void addParam(UriComponentsBuilder builder, String key, Collection<? extends CharSequence> value) {
        if (!nullOrEmpty(value)) {
            builder.queryParam(key, String.join(",", value));
        }
    }

    private void addGeoQueryParams(UriComponentsBuilder builder, GeoQuery geoQuery) {
        if (geoQuery != null) {
            StringBuilder georel = new StringBuilder(geoQuery.getRelation().name());
            if (geoQuery.getRelation() == GeoQuery.Relation.near) {
                georel.append(';').append(geoQuery.getModifier());
                georel.append(URLEncoder.encode("==")).append(geoQuery.getDistance()); //adjustments to NEC's broker
            }
            builder.queryParam("georel", georel.toString());
            builder.queryParam("geometry", geoQuery.getGeometry());
            String coords = geoQuery.getCoordinates().stream().map(o->"["+o.toString()+"]").collect(Collectors.joining(";"));
            builder.queryParam("coordinates", coords);  //adjustments to NEC's broker
            String abc = "123";
        }
    }

    private boolean nullOrEmpty(Collection i) {
        return i == null || i.isEmpty();
    }

    private boolean nullOrEmpty(String i) {
        return i == null || i.isEmpty();
    }

    private int extractTotalCount(ResponseEntity responseEntity) {
        String total = responseEntity.getHeaders().getFirst("X-Total-Count");
        try {
            return Integer.parseInt(total);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String extractId(ResponseEntity responseEntity) {
        String location = responseEntity.getHeaders().getFirst("Location");
        String paths[] = location.split("/");
        if (paths != null && paths.length > 0) {
            return paths[paths.length - 1];
        }
        return "";
    }

    /**
     * @return return a clone HttpHeader from default HttpHeader
     */
    private HttpHeaders cloneHttpHeaders() {
        HttpHeaders httpHeaders = getHttpHeaders();
        HttpHeaders clone = new HttpHeaders();
        for (String entry : httpHeaders.keySet()) {
            clone.put(entry, httpHeaders.get(entry));
        }
        return clone;
    }

    /**
     * Inject the Ngsi2ResponseErrorHandler
     */
    protected void injectNgsi2ErrorHandler() {
        MappingJackson2HttpMessageConverter converter = getMappingJackson2HttpMessageConverter();
        if (converter != null) {
            this.asyncRestTemplate.setErrorHandler(new Ngsi2ResponseErrorHandler(converter.getObjectMapper()));
        }
    }

    /**
     * Inject an ObjectMapper supporting Java8 and JavaTime module by default
     */
    protected void injectJava8ObjectMapper() {
        MappingJackson2HttpMessageConverter converter = getMappingJackson2HttpMessageConverter();
        if (converter != null) {
            converter.getObjectMapper().registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
    }

    private MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        for(HttpMessageConverter httpMessageConverter : asyncRestTemplate.getMessageConverters()) {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                return (MappingJackson2HttpMessageConverter)httpMessageConverter;
            }
        }
        return null;
    }
}
