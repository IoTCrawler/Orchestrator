package com.agtinternational.iotcrawler.core.interfaces;

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

//import com.agtinternational.iotcrawler.core.models.NGSI_LD.EntityLD;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.Notification;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class IoTCrawlerClient implements Component {

    /////////////////////////////////////Streams
    public IoTStream getStreamById(String id) throws Exception{
        return (IoTStream) getEntityById(id, IoTStream.class);
    }

    public List<IoTStream> getStreams(Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception{
        return getEntities(IoTStream.class, query, ranking, offset, limit);
    }


    /////////////////////////////////////Sensors
    public List<Sensor> getSensors(Map<String, Object> query, int offset, int limit) throws Exception{
        return getEntities(Sensor.class, query, null, offset, limit);
    }

    /////////////////////////////////////Platforms
    public List<Platform> getPlatforms(Map<String, Object> query, int offset, int limit) throws Exception{
        return getEntities(Platform.class, query, null, offset, limit);
    }

    /////////////////////////////////////Properties
    public List<ObservableProperty> getObservableProperties(Map<String, Object> query, int offset, int limit) throws Exception{
        return getEntities(ObservableProperty.class, query, null, offset, limit);
    }


    /////////////////////////////////////Observations
    public List<StreamObservation> getObservations(Map<String, Object> query,  int offset, int limit) throws Exception{
        return getEntities(StreamObservation.class, query, null, offset, limit);
    }


    public abstract List<String> getEntityURIs(Map<String, Object> query, int offset, int limit) throws Exception;

    public <T> Object getEntityById(String id, Class<T> targetClass) throws Exception{
        EntityLD entity = getEntityById(id);
        T targetEnt = (T) Utils.convertEntityToTargetClass(entity, targetClass);
        return targetEnt;
    }


//        List<EntityLD> entities = getEntities(Utils.getTypeURI(targetClass), query, ranking, offset, limit);
//        return Utils.convertEntitiesToType(entities, targetClass);
//    }

    //public abstract List<EntityLD> getEntitiesById(String[] ids, String entityType) throws Exception;
    public abstract EntityLD getEntityById(String id) throws Exception;
    public abstract List<EntityLD> getEntities(String entityType, Map<String, Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception;
    public abstract  <T> List<T> getEntities(Class<T> targetClass, Map<String, Object> query,  Map<String, Number> ranking, int offset, int limit) throws Exception;


//    public Boolean registerStream(IoTStream ioTStream) throws Exception { return registerEntity(ioTStream); }
//
//    public abstract Boolean registerEntity(RDFModel model) throws Exception;
    //public abstract Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception;
    public abstract String subscribeToStream(String streamId, Function<Notification, Void> onChange) throws Exception;
    public abstract String subscribe(Subscription subscription, Function<byte[], Void> onChange) throws Exception;
}
