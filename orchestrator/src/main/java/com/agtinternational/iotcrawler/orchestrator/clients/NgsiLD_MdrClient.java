package com.agtinternational.iotcrawler.orchestrator.clients;

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;

import com.orange.ngsi2.model.Paginated;
import org.apache.commons.lang.NotImplementedException;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.HttpClientErrorException;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import static com.agtinternational.iotcrawler.core.Utils.convertEntitiesToType;
import static com.agtinternational.iotcrawler.core.Utils.getTypeURI;
import static com.agtinternational.iotcrawler.orchestrator.Constants.NGSILD_BROKER_URI;

public class NgsiLD_MdrClient extends AbstractMetadataClient {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    NgsiLDClient ngsiLDClient;
    //String serverUrl = "http://155.54.95.248:9090/ngsi-ld/";

    public NgsiLD_MdrClient(){
        String serverUrl = (System.getenv().containsKey(NGSILD_BROKER_URI)? System.getenv(NGSILD_BROKER_URI): "http://localhost:3000/ngsi-ld/");

        LOGGER.info("Initializing NgsiLD_MdrClient at {}", serverUrl);
        ngsiLDClient = new NgsiLDClient(serverUrl);
    }


//    public List<IoTStream> getStreams(String query){
////
//        throw new NotImplementedException();
//
//        //List<IoTStream>  ret = new ArrayList<>();
//
////        List<String> ids = null;
////        List<String> types = null;
////        List<String> props = null;
////
////        int limit = 0;
//
////        if(selector.getSubject()!=null) {
////            ids = Arrays.asList(new String[]{selector.getSubject()});
//////            try {
//////                Entity entity = ngsiLDClient.getEntitiesAsType(selector.getSubject(), null, null).get();
//////                IoTStream ioTStream = IoTStream.fromEntity(entity);
//////                ret.add(ioTStream);
//////            } catch (InterruptedException e) {
//////                e.printStackTrace();
//////            } catch (ExecutionException e) {
//////                e.printStackTrace();
//////            }
////
////        }else if(selector.getPredicate()!=null){
////            if(selector.getPredicate().trim().equals("a") || selector.getPredicate().equals(RDF.type.getURI()))
////                types = Arrays.asList(new String[]{ selector.getObject() });
////            else {
////                props = Arrays.asList(new String[]{URI.create(selector.getPredicate()).getFragment() +"="+ selector.getObject()});
////            }
////        }else{
////            //Arrays.asList(new String[]{ "http://example.org/common/isParked" })
////
////        }
//
////        try {
////            //entities = ngsiLDClient.getEntitiesAsType(String entityId, String type, Collection<String> attrs);
////            Paginated<Entity> entities = ngsiLDClient.getEntities(ids, null, types, props, 0, limit, false).get();
////            for(Entity entity: entities.getItems()) {
////                IoTStream ioTStream = IoTStream.fromEntity(entity);
////                ret.add(ioTStream);
////            }
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        } catch (ExecutionException e) {
////            e.printStackTrace();
////        }
////
////        return ret;
//    }

    @Override
    public List<String> getEntityURIs(String query) {
        throw new NotImplementedException();
    }

    @Override
    public List<EntityLD> getEntities(String query){
        throw new NotImplementedException();
    }

    @Override
    public List<EntityLD> getEntities(String type, String query, int offset, int limit) throws Exception {
        String[] types = new String[]{type};
        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(types), null,query,null, null, offset, limit, false).get();
        return (List<EntityLD>)paginated.getItems();
    }

    @Override
    public List<EntityLD> getEntities(String[] ids) throws Exception {
        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntities(Arrays.asList(ids), null, null, null, 0, 0, false).get();
        return (List<EntityLD>)paginated.getItems();
    }

    @Override
    public List<EntityLD> getEntities(String[] ids, String typeURI) throws Exception {
        String[] types = new String[]{typeURI};
        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntities(Arrays.asList(ids), null, Arrays.asList(types), null, 0, 0, false).get();
        return (List<EntityLD>)paginated.getItems();
    }

    @Override
    public List<EntityLD> getEntities(String typeURI, int offset, int limit) throws Exception {
        String[] types = new String[]{typeURI};
        Paginated<EntityLD> paginated = null;
        paginated = ngsiLDClient.getEntities(null, null, Arrays.asList(types), null, offset, limit, false).get();
        return (List<EntityLD>)paginated.getItems();
    }

//    @Override
//    public <T> List<T> getEntities(Class<T> targetClass, int limit) throws Exception {
//
//        String type = getTypeURI(targetClass);
//        List<EntityLD> entities =  getEntities(type, limit);
//        List<T> ret = convertEntitiesToType(entities, targetClass);
//
//        return ret;
//    }

    @Override
    public <T> List<T> getEntities(String[] ids, Class<T> targetClass) throws Exception {
        Paginated<EntityLD> entities = null;
        String type = getTypeURI(targetClass);
        entities = ngsiLDClient.getEntities(Arrays.asList(ids), null, Arrays.asList(type), null, 0, 0, false).get();

        List<T> ret = convertEntitiesToType(entities.getItems(), targetClass);
        return ret;
    }



//    @Override
//    public <T> List<T> getEntities(Class<T> type, String query) {
//        throw new NotImplementedException();
//    }

    @Override
    public Boolean registerEntity(RDFModel entityAsModel) throws Exception{
        LOGGER.info("registerEntity {}", entityAsModel.getURI());
        Semaphore reqFinished = new Semaphore(0);
//        Semaphore deleteFinished = new Semaphore(0);
        List<Boolean> ret = new ArrayList<>();
        List<Exception> exception = new ArrayList<>();
//        boolean updating = false;
        try {
            EntityLD entity = entityAsModel.toEntityLD();

//            // Check if it already exists in the MDR
//            String[] id = new String[]{ entity.getId() };
//            Paginated<EntityLD> entities =
//                    ngsiLDClient.getEntities(Arrays.asList(id), null, null, null,0, 0,
//                            false).get();
//            updating = entities.getItems().size() != 0;

            ListenableFuture<Void> req;
//            if(updating) {
//                ngsiLDClient.deleteEntity(entity.getId(), null).addCallback(
//                        aVoid -> deleteFinished.release(),
//                        aVoid -> deleteFinished.release());
//                deleteFinished.acquire();
//            }

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
                    if(throwable instanceof HttpClientErrorException && ((HttpClientErrorException) throwable).getStatusCode().value()==409) {
                        Exception exception1 = new InstanceAlreadyExistsException();
                        LOGGER.debug("Entity {} already exists", entity.getId());
                    }else{
                        Exception e = new Exception("Failed to add entity "+ entity.getId()+": "+ throwable.getLocalizedMessage());
                        exception.add(e);
                    }
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
