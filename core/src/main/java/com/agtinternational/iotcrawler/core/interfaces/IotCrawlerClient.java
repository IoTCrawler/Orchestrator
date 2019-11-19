package com.agtinternational.iotcrawler.core.interfaces;

//import com.agtinternational.iotcrawler.core.models.NGSI_LD.EntityLD;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.util.List;
import java.util.function.Function;

public abstract class IotCrawlerClient implements Component {

    /////////////////////////////////////Streams
    public List<IoTStream> getStreamsById(String[] ids) throws Exception{
        return getEntitiesById(ids, IoTStream.class);
    }

    public List<IoTStream> getStreams(String query, int offset, int limit) throws Exception{
        return getEntities(IoTStream.class, query, offset, limit);
    }


    /////////////////////////////////////Sensors
    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception{
        return getEntities(Sensor.class, query, offset, limit);
    }

    /////////////////////////////////////Platforms
    public List<SosaPlatform> getPlatforms(String query, int offset, int limit) throws Exception{
        return getEntities(SosaPlatform.class, query, offset, limit);
    }

    /////////////////////////////////////Properties
    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception{
        return getEntities(ObservableProperty.class, query, offset, limit);
    }


    /////////////////////////////////////Observations
    public List<StreamObservation> getObservations(String streamId,  int offset, int limit) throws Exception{
        return getEntities(StreamObservation.class, null, offset, limit);
    }


    public abstract List<String> getEntityURIs(String query, int offset, int limit) throws Exception;

    public <T> List<T> getEntitiesById(String[] ids, Class<T> targetClass) throws Exception{
        List<EntityLD> entities = getEntitiesById(ids, Utils.getTypeURI(targetClass));
        return Utils.convertEntitiesToType(entities, targetClass);
    }

    public <T> List<T> getEntities(Class<T> targetClass, String query, int offset, int limit) throws Exception{
        List<EntityLD> entities = getEntities(Utils.getTypeURI(targetClass), query, offset, limit);
        return Utils.convertEntitiesToType(entities, targetClass);
    }

    public abstract List<EntityLD> getEntitiesById(String[] ids, String entityType) throws Exception;
    public abstract List<EntityLD> getEntities(String entityType, String query, int offset, int limit) throws Exception;

    public abstract Boolean registerEntity(RDFModel model) throws Exception;
    public abstract Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception;
    public abstract String subscribeTo(String streamId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception;
}
