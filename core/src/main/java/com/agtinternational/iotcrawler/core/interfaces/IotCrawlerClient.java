package com.agtinternational.iotcrawler.core.interfaces;

//import com.agtinternational.iotcrawler.core.models.NGSI_LD.EntityLD;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.util.List;
import java.util.function.Function;

public abstract class IotCrawlerClient implements Component {

    public List<IoTStream> getStreams(int offset, int limit) throws Exception{
        return getEntities(IoTStream.class, offset, limit);
    }
    public List<IoTStream> getStreams(String query, int offset, int limit) throws Exception{
        return getEntities(IoTStream.class, query, offset, limit);
    }

    public List<IoTStream> getStreams(String[] ids) throws Exception{
        return getEntities(ids, IoTStream.class);
    }

    public List<Sensor> getSensors(int offset, int limit) throws Exception{
        return getEntities(Sensor.class, offset, limit);
    }
    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception{
        return getEntities(Sensor.class, query, offset, limit);
    }

    public List<SosaPlatform> getPlatforms(int offset, int limit) throws Exception{
        return getEntities(SosaPlatform.class, offset, limit);
    }
    public List<SosaPlatform> getPlatforms(String query, int offset, int limit) throws Exception{
        return getEntities(SosaPlatform.class, query, offset, limit);
    }

    public List<ObservableProperty> getObservableProperties(int offset, int limit) throws Exception{
        return getEntities(ObservableProperty.class, offset, limit);
    }
    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception{
        return getEntities(ObservableProperty.class, query, offset, limit);
    }

//    public List<StreamObservation> getObservations() throws Exception;
    public List<StreamObservation> getObservations(String streamId,  int offset, int limit) throws Exception{
        return getEntities(StreamObservation.class, offset, limit);
    }


    public abstract List<String> getEntityURIs(String query) throws Exception;

    public <T> List<T> getEntities(String[] ids, Class<T> targetClass) throws Exception{
        List<EntityLD> entities = getEntities(ids, Utils.getTypeURI(targetClass));
        return Utils.convertEntitiesToType(entities, targetClass);
    }
    public <T> List<T> getEntities(Class<T> targetClass, int offset, int limit) throws Exception{
        List<EntityLD> entities = getEntities(Utils.getTypeURI(targetClass), offset, limit);
        return Utils.convertEntitiesToType(entities, targetClass);
    }

    public <T> List<T> getEntities(Class<T> targetClass, String query, int offset, int limit) throws Exception{
        List<EntityLD> entities = getEntities(Utils.getTypeURI(targetClass), query, offset, limit);
        return Utils.convertEntitiesToType(entities, targetClass);
    }

    //public abstract List<EntityLD> getEntities(String[] id) throws Exception;  //prohibited in scorpio
    public abstract List<EntityLD> getEntities(String[] ids, String entityType) throws Exception;
    public abstract List<EntityLD> getEntities(String entityType, int offset, int limit) throws Exception;
    public abstract List<EntityLD> getEntities(String entityType, String query, int offset, int limit) throws Exception;

    public abstract Boolean registerEntity(RDFModel model) throws Exception;
    public abstract Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception;
    public abstract String subscribeTo(String streamId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception;
}
