//package com.agtinternational.iotcrawler.orchestrator.clients;

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
//
//
//import com.agtinternational.iotcrawler.core.Utils;
//import com.agtinternational.iotcrawler.fiware.models.EntityLD;
//
//import com.agtinternational.iotcrawler.core.models.*;
//import com.orange.ngsi2.model.Entity;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.rdf.model.Model;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public class TripleStoreMDRClient extends AbstractMetadataClient {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TripleStoreMDRClient.class);
//    TripleStoreClient tripleStoreClient;
//
//    public TripleStoreMDRClient(){
//        tripleStoreClient = new TripleStoreClient();
//
//    }
//
//    @Override
//    public List<String> getEntityURIs(String query){
//        List<String> ids = new ArrayList<>();
//        tripleStoreClient.execQuery(query, new Function<ResultSet, Void>() {
//            @Override
//            public Void apply(ResultSet resultSet) {
//                List<String> uris = tripleStoreClient.getResultSetAsStringList(resultSet, "?s", false);
//                ids.addAll(uris);
//                return null;
//            }
//        });
//        return ids;
//    }
//
//    @Override
//    public Boolean registerEntity(RDFModel entitiy) {
//
//        return registerEntity(entitiy.getURI(), entitiy.getModel());
//    }
//
//    @Override
//    public Boolean registerEntity(String uri, Model model) {
//        Model originalModel = tripleStoreClient.queryModel(URI.create(uri));
//        return tripleStoreClient.updateModel(originalModel, model);
//    }
//
////    @Override
////    public List<IoTStream> getStreams(int limit) {
////        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <"+IoTStream.getTypeUri()+"> . }" +(limit>0?" LIMIT "+limit:""));
////        List<String> filter = new ArrayList<>();
////        for(String uri: uris)
////            filter.add("<"+uri+">");
////        return getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN ("+String.join(",", filter)+")) }");
////    }
////
////
////    @Override
////    public List<IoTStream> getStreams(String query){
////
////        List<IoTStream> ret = new ArrayList<>();
////        Map<String, Model> models = tripleStoreClient.queryModel(query);
////        for(String uri : models.keySet())
////        try {
////            ret.add(new IoTStream(uri, models.get(uri)));
////        }
////        catch (Exception e){
////            LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
////            e.printStackTrace();
////        }
////
////        return ret;
////    }
//
//    @Override
//    public List<EntityLD> getEntities(String query) {
//
//        Map<String, Model> modelsMap = tripleStoreClient.queryModel(query);
//        List<EntityLD> ret = new ArrayList<>();
//        for(String uri: modelsMap.keySet()) {
//            try {
//                RDFModel rdfModel = new RDFModel(uri, modelsMap.get(uri));
//                EntityLD entityLD = rdfModel.toEntityLD();
//                ret.add(entityLD);
//            }
//            catch (Exception e){
//                LOGGER.error("Failed to create EntityLD for {}", uri);
//            }
//        }
//        return ret;
//    }
//
//    @Override
//    public List<EntityLD> getEntities(String typeUri, int limit) {
//        String uri=null;
//
////        if(type.equals(IoTStream.class))
////            uri = IoTStream.getTypeUri();
////
////        if(type.equals(Sensor.class))
////            uri = Sensor.getTypeUri();
////
////        if(type.equals(IoTPlatform.class))
////            uri = IoTPlatform.getTypeUri();
////
////        if(type.equals(ObservableProperty.class))
////            uri = ObservableProperty.getTypeUri();
////
////        if(uri==null)
////            LOGGER.error("No uri for {}", type.getSimpleName());
//
//        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <" + uri + "> . }" + (limit > 0 ? " LIMIT " + limit : ""));
//        List<String> filter = new ArrayList<>();
//        for (String uri1 : uris)
//            filter.add("<" + uri1 + ">");
//
//        return getEntities(typeUri, "SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN (" + String.join(",", filter) + ")) }");
//    }
//
//    @Override
//    public List<EntityLD> getEntities(String typeUri, String query){
//        List<EntityLD> ret = new ArrayList<>();
//        Map<String, Model> models = tripleStoreClient.queryModel(query);
//        for(String uri : models.keySet())
//            try {
//                EntityLD toAdd=null;
//
//                if(typeUri.equals(IoTStream.getTypeUri()))
//                    toAdd = new IoTStream(uri, models.get(uri)).toEntityLD();
//
//                if(typeUri.equals(Sensor.getTypeUri()))
//                    toAdd = new Sensor(uri, models.get(uri)).toEntityLD();
//
//                if(typeUri.equals(IoTPlatform.getTypeUri()))
//                    toAdd = new IoTPlatform(uri, models.get(uri)).toEntityLD();
//
//                if(typeUri.equals(ObservableProperty.getTypeUri()))
//                    toAdd = new ObservableProperty(uri, models.get(uri)).toEntityLD();
//
//                if(toAdd!=null)
//                    ret.add(toAdd);
//                else
//                    throw new Exception("No suitable type for entity " + typeUri);
//            }
//            catch (Exception e){
//                LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
//                e.printStackTrace();
//            }
//
//        return ret;
//    }
//
//    @Override
//    public <T> List<T> getEntitiesAsType(Class<T> type, int limit) {
//
//        String uri=null;
//
//        if(type.equals(IoTStream.class))
//            uri = IoTStream.getTypeUri();
//
//        if(type.equals(Sensor.class))
//            uri = Sensor.getTypeUri();
//
//        if(type.equals(IoTPlatform.class))
//            uri = IoTPlatform.getTypeUri();
//
//        if(type.equals(ObservableProperty.class))
//            uri = ObservableProperty.getTypeUri();
//
//        if(uri==null)
//            LOGGER.error("No uri for {}", type.getSimpleName());
//
//        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <" + uri + "> . }" + (limit > 0 ? " LIMIT " + limit : ""));
//        List<String> filter = new ArrayList<>();
//        for (String uri1 : uris)
//            filter.add("<" + uri1 + ">");
//        return getEntitiesAsType(type, "SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN (" + String.join(",", filter) + ")) }");
//    }
//
//    @Override
//    public <T> List<T> getEntitiesAsType(Class<T> type, String query) {
//        List<T> ret = new ArrayList<>();
//        Map<String, Model> models = tripleStoreClient.queryModel(query);
//        for(String uri : models.keySet())
//            try {
//                T toAdd=null;
//
//                if(type.equals(IoTStream.class))
//                    toAdd = (T)new IoTStream(uri, models.get(uri));
//
//                if(type.equals(Sensor.class))
//                    toAdd = (T)new Sensor(uri, models.get(uri));
//
//                if(type.equals(IoTPlatform.class))
//                    toAdd = (T)new IoTPlatform(uri, models.get(uri));
//
//                if(type.equals(ObservableProperty.class))
//                    toAdd = (T)new ObservableProperty(uri, models.get(uri));
//
//                if(toAdd!=null)
//                    ret.add(toAdd);
//                else
//                    throw new Exception("No suitable type for entity " + type.getSimpleName());
//            }
//            catch (Exception e){
//                LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
//                e.printStackTrace();
//            }
//
//        return ret;
//    }
//
////    @Override
////    public List<RDFModel> getEntities(String query) {
////        return tripleStoreClient.execQuery(query, );
////    }
//
////    @Override
////    public List<RDFModel> getEntities(FilteringSelector selector, int limit) {
////        return tripleStoreClient.
////    }
//
////    @Override
////    public List<IoTPlatform> getSensors() {
////        return getSensors(0);
////    }
//
////    @Override
////    public List<Sensor> getSensors(int limit){
////        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <"+Sensor.getTypeUri()+"> . }" +(limit>0?" LIMIT "+limit:""));
////        List<String> filter = new ArrayList<>();
////        for(String uri: uris)
////            filter.add("<"+uri+">");
////        return getSensors("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN ("+String.join(",", filter)+")) }");
////    }
////
////    @Override
////    public List<Sensor> getSensors(String query) {
////        List<Sensor> ret = new ArrayList<>();
////        Map<String, Model> models = tripleStoreClient.queryModel(query);
////        for(String uri : models.keySet())
////            try {
////                ret.add(new Sensor(uri, models.get(uri)));
////            }
////            catch (Exception e){
////                LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
////                e.printStackTrace();
////            }
////        return ret;
////    }
////
////    @Override
////    public List<IoTPlatform> getPlatforms(int limit){
////        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <"+IoTPlatform.getTypeUri()+"> . }" +(limit>0?" LIMIT "+limit:""));
////        List<String> filter = new ArrayList<>();
////        for(String uri: uris)
////            filter.add("<"+uri+">");
////        return getPlatforms("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN ("+String.join(",", filter)+")) }");
////    }
////
////    @Override
////    public List<IoTPlatform> getPlatforms(String query) {
////        List<IoTPlatform> ret = new ArrayList<>();
////        Map<String, Model> models = tripleStoreClient.queryModel(query);
////        for(String uri : models.keySet())
////            try {
////                ret.add(new IoTPlatform(uri, models.get(uri)));
////            }
////            catch (Exception e){
////                LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
////                e.printStackTrace();
////            }
////        return ret;
////    }
////
////    @Override
////    public List<ObservableProperty> getObservableProperties(int limit){
////        List<String> uris = getEntityURIs("SELECT ?s ?p ?o WHERE { ?s a <"+ObservableProperty.getTypeUri()+"> . }" +(limit>0?" LIMIT "+limit:""));
////        List<String> filter = new ArrayList<>();
////        for(String uri: uris)
////            filter.add("<"+uri+">");
////        return getObservableProperties("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?s IN ("+String.join(",", filter)+")) }");
////    }
////
////    @Override
////    public List<ObservableProperty> getObservableProperties(String query) {
////        List<ObservableProperty> ret = new ArrayList<>();
////        Map<String, Model> models = tripleStoreClient.queryModel(query);
////        for(String uri : models.keySet())
////            try {
////                ret.add(new ObservableProperty(uri, models.get(uri)));
////            }
////            catch (Exception e){
////                LOGGER.error("Failed to instantiate a stream from the model: {}", Utils.writeModel2JsonLDString(models.get(uri)));
////                e.printStackTrace();
////            }
////        return ret;
////    }
//
////    @Override
////    public List<StreamObservation> getObservations() {
////        return null;
////    }
////
////    @Override
////    public List<StreamObservation> getObservations(int limit) {
////        return null;
////    }
//
//
//
//}
