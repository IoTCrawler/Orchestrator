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
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;

import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class IotCrawlerClient implements Component {

    /////////////////////////////////////Streams
    public List<IoTStream> getStreamsById(String[] ids) throws Exception{
        return getEntitiesById(ids, IoTStream.class);
    }

    public List<IoTStream> getStreams(String query, Map<String, Number> ranking, int offset, int limit) throws Exception{
        return getEntities(IoTStream.class, query, ranking, offset, limit);
    }


    /////////////////////////////////////Sensors
    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception{
        return getEntities(Sensor.class, query, null, offset, limit);
    }

    /////////////////////////////////////Platforms
    public List<Platform> getPlatforms(String query, int offset, int limit) throws Exception{
        return getEntities(Platform.class, query, null, offset, limit);
    }

    /////////////////////////////////////Properties
    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception{
        return getEntities(ObservableProperty.class, query, null, offset, limit);
    }


    /////////////////////////////////////Observations
    public List<StreamObservation> getObservations(String streamId,  int offset, int limit) throws Exception{
        return getEntities(StreamObservation.class, streamId, null, offset, limit);
    }


    public abstract List<String> getEntityURIs(String query, int offset, int limit) throws Exception;

    public <T> List<T> getEntitiesById(String[] ids, Class<T> targetClass) throws Exception{
        List<EntityLD> entities = getEntitiesById(ids, Utils.getTypeURI(targetClass));
        return Utils.convertEntitiesToTargetClass(entities, targetClass);
    }


//        List<EntityLD> entities = getEntities(Utils.getTypeURI(targetClass), query, ranking, offset, limit);
//        return Utils.convertEntitiesToType(entities, targetClass);
//    }

    public abstract List<EntityLD> getEntitiesById(String[] ids, String entityType) throws Exception;
    public abstract List<EntityLD> getEntities(String entityType, String query, Map<String, Number> ranking, int offset, int limit) throws Exception;
    public abstract  <T> List<T> getEntities(Class<T> targetClass, String query,  Map<String, Number> ranking, int offset, int limit) throws Exception;


//    public Boolean registerStream(IoTStream ioTStream) throws Exception { return registerEntity(ioTStream); }
//
//    public abstract Boolean registerEntity(RDFModel model) throws Exception;
    //public abstract Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception;
    //public abstract String subscribeTo(String streamId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception;
    public abstract String subscribeTo(Subscription subscription, Function<StreamObservation, Void> onChange) throws Exception;
}
