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

import com.agtinternational.iotcrawler.core.models.RDFModel;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public abstract class AbstractMetadataClient {

    //public abstract void registerStream(IoTStream stream);
    public abstract Boolean registerEntity(RDFModel entitiy) throws Exception;
    public abstract Boolean registerEntity(String uri, Model entitiy) throws Exception;

    public abstract List<String> getEntityURIs(Map<String,Object> query);

    //public abstract List<EntityLD> getEntities(String[] ids) throws Exception;
    public abstract EntityLD getEntityById(String id) throws Exception;
    public abstract List<EntityLD> getEntities(String type, Map<String,Object> query, Map<String, Number> ranking, int offset, int limit) throws Exception;

    public abstract String getBrokerHost();

    public abstract String subscribeTo(Subscription subscription);

    //public abstract List<EntityLD> getEntities(String query);
    //public abstract <T> List<T> getEntities(String[] ids, Class<T> targetClass) throws Exception;
//    public abstract <T> List<T> getEntities(Class<T> type, int limit) throws Exception;
//    public abstract <T> List<T> getEntities(Class<T> type, String query);

}
