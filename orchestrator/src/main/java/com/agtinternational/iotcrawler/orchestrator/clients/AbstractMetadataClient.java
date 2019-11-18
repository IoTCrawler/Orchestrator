package com.agtinternational.iotcrawler.orchestrator.clients;

import com.agtinternational.iotcrawler.core.models.RDFModel;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AbstractMetadataClient {

    //public abstract void registerStream(IoTStream stream);
    public abstract Boolean registerEntity(RDFModel entitiy) throws Exception;
    public abstract Boolean registerEntity(String uri, Model entitiy) throws Exception;

    public abstract List<String> getEntityURIs(String query);

    //public abstract List<EntityLD> getEntities(String[] ids) throws Exception;
    public abstract List<EntityLD> getEntitiesById(String[] ids, String typeURI) throws Exception;
    public abstract List<EntityLD> getEntities(String type, JsonObject query, int offset, int limit) throws Exception;

    //public abstract List<EntityLD> getEntities(String query);
    //public abstract <T> List<T> getEntities(String[] ids, Class<T> targetClass) throws Exception;
//    public abstract <T> List<T> getEntities(Class<T> type, int limit) throws Exception;
//    public abstract <T> List<T> getEntities(Class<T> type, String query);

}
