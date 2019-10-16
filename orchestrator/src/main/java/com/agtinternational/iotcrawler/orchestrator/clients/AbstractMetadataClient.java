package com.agtinternational.iotcrawler.orchestrator.clients;

import com.agtinternational.iotcrawler.core.models.RDFModel;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;

import java.util.List;

public abstract class AbstractMetadataClient {

    //public abstract void registerStream(IoTStream stream);
    public abstract Boolean registerEntity(RDFModel entitiy) throws Exception;
    public abstract Boolean registerEntity(String uri, Model entitiy) throws Exception;

    public abstract List<String> getEntityURIs(String query);
    public abstract List<EntityLD> getEntities(String query);

    public abstract List<EntityLD> getEntities(String type, String query);
    public abstract List<EntityLD> getEntities(String type, int limit) throws Exception;

    public abstract <T> List<T> getEntitiesAsType(Class<T> type, int limit) throws Exception;
    public abstract <T> List<T> getEntitiesAsType(Class<T> type, String query);

//    public abstract List<IoTStream> getStreams(int limit);
//    public abstract List<IoTStream> getStreams(String query);
//
//    public abstract List<Sensor> getSensors(int limit);
//    public abstract List<Sensor> getSensors(String query);
//
//    public abstract List<IoTPlatform> getPlatforms(int limit);
//    public abstract List<IoTPlatform> getPlatforms(String query);
//
//    public abstract List<ObservableProperty> getObservableProperties(int limit);
//    public abstract List<ObservableProperty> getObservableProperties(String query);

//    public abstract List<StreamObservation> getObservations();
//    public abstract List<StreamObservation> getObservations(int limit);

}
