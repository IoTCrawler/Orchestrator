package com.agtinternational.iotcrawler.orchestrator.clients;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
