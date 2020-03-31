package com.agtinternational.iotcrawler.orchestrator.clients;

/*-
 * #%L
 * orchestrator
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
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;

import com.agtinternational.iotcrawler.fiware.models.subscription.SubscriptionLD;
import com.orange.ngsi2.model.Paginated;
import org.apache.commons.lang.NotImplementedException;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import org.springframework.web.client.HttpClientErrorException;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;
import java.util.concurrent.Semaphore;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;


public class NgsiLD_MdrClient extends AbstractMetadataClient {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String brokerUrl;
    private String brokerHost;
    NgsiLDClient ngsiLDClient;
    boolean cutURIs = false;
    //String serverUrl = "http://155.54.95.248:9090/ngsi-ld/";

//    public NgsiLD_MdrClient(){
//        this((System.getenv().containsKey(NGSILD_BROKER_URL)? System.getenv(NGSILD_BROKER_URL): "http://localhost:3000/ngsi-ld/"));
//    }

    public NgsiLD_MdrClient(String brokerUrl, Boolean cutURIs){
        brokerHost = brokerUrl.split("/ngsi-ld")[0];
        this.cutURIs = cutURIs;

        LOGGER.debug("Initializing NgsiLD_MdrClient to {}. CurURIs={}", brokerUrl, cutURIs);
        ngsiLDClient = new NgsiLDClient(brokerUrl);
    }


    public String getBrokerHost() {
        return brokerHost;
    }

    @Override
    public String subscribeTo(SubscriptionLD subscriptionLD) {
        ListenableFuture<String> req = ngsiLDClient.addSubscription(subscriptionLD);

        final String[] ret = { null };
        Semaphore reqFinished = new Semaphore(0);
        req.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String id) {
                ret[0] = id;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Failed to create subscription");
                throwable.printStackTrace();
                reqFinished.release();
            }

        });

        try {
            reqFinished.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret[0];
    }

    @Override
    public List<String> getEntityURIs(String query) {
        throw new NotImplementedException();
    }

    @Override
    public List<EntityLD> getEntitiesById(String[] ids, String type) throws Exception {
        if(cutURIs)
            type = Utils.cutURL(type, RDFModel.getNamespaces());

        String[] types = new String[]{type};
        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntitiesSync((ids!=null?Arrays.asList(ids):null), null, Arrays.asList(types), null, 0, 0, false);
        return (List<EntityLD>)paginated.getItems();
    }

    @Override
    public List<EntityLD> getEntities(String type, String query, Map<String, Number> ranking, int offset, int limit) throws Exception {

        if(cutURIs)
            type = Utils.cutURL(type, RDFModel.getNamespaces());

        String[] types = new String[]{  type };

        List<String> pairs = (query!=null?Arrays.asList(query.split("&")):new ArrayList<>());

        if(ranking!=null)
            for(String key: ranking.keySet())
                pairs.add("rankWeights["+key+"]="+ ranking.get(key).toString());
        query = String.join("&", pairs);

        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(types), null, query,null, null, offset, limit, false).get();
        return (List<EntityLD>)paginated.getItems();
    }




    @Override
    public Boolean registerEntity(RDFModel rdfModel) throws Exception{
        LOGGER.info("registerEntity {}", rdfModel.getURI());
        Semaphore reqFinished = new Semaphore(0);
//        Semaphore deleteFinished = new Semaphore(0);
        List<Boolean> ret = new ArrayList<>();
        List<Exception> exception = new ArrayList<>();
//        boolean updating = false;
        try {
            EntityLD entity = rdfModel.toEntityLD(cutURIs);

            ListenableFuture<Void> req;

            req = ngsiLDClient.addEntity(entity);
            req.addCallback(new ListenableFutureCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    LOGGER.debug("Entity added {}", entity.getId());
                    ret.add(true);
                    reqFinished.release();
                }

                @Override
                public void onFailure(Throwable throwable) {
//                    if(throwable instanceof HttpClientErrorException && ((HttpClientErrorException) throwable).getStatusCode().value()==409) {
//                        Exception exception1 = new InstanceAlreadyExistsException();
//                        LOGGER.debug("Entity {} already exists", entity.getId());
//                    }else{
//                        Exception e = new Exception("Failed to add entity "+ entity.getId()+": "+ throwable.getLocalizedMessage());
//                        exception.add(e);
//                    }
                    Exception e = new Exception("Failed to add entity "+ entity.getId()+": "+ throwable.getLocalizedMessage());
                    exception.add(e);
                    ret.add(false);
                    reqFinished.release();
                }

            });
            reqFinished.acquire();
        }catch (InstanceAlreadyExistsException e){
            LOGGER.warn(e.getLocalizedMessage());
        }
        catch (Exception e){
            LOGGER.error("Failed to register entity: {}", e.getLocalizedMessage());
            e.printStackTrace();
            throw e;
        }
//        if(exception.size()>0)
//            throw exception.get(0);
        return ret.get(0);
    }

    @Override
    public Boolean registerEntity(String uri, Model entitiy) {
        throw new NotImplementedException();
    }

}
