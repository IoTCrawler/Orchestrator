//package com.agtinternational.iotcrawler.core.interfaces;
//
///*-
// * #%L
// * core
// * %%
// * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
// * %%
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * #L%
// */
//
//import com.agtinternational.iotcrawler.core.OrchestratorRPCClient;
//import com.agtinternational.iotcrawler.core.models.*;
//
//import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
//import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public abstract class IoTCrawlerApp extends IotCrawlerClient implements Component {
//
//    protected IotCrawlerClient iotCrawlerClient;
//
//    public IoTCrawlerApp(){
//        iotCrawlerClient = new OrchestratorRPCClient();
//    }
//
//    public void init() throws Exception{
//        iotCrawlerClient.init();
//    }
//
//    public void run() throws Exception{
//        iotCrawlerClient.run();
//    }
//
//
//    @Override
//    public Boolean registerEntity(RDFModel entity) throws Exception {
//        return iotCrawlerClient.registerEntity(entity);
//    }
//
////    @Override
////    public List<IoTStream> getStreams(int offset, int limit) throws Exception {
////        return iotCrawlerClient.getStreams(offset, limit);
////    }
//
//    @Override
//    public List<IoTStream> getStreams(String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getStreams(query, ranking, offset, limit);
//    }
//
////    @Override
////    public List<Sensor> getSensors(int offset, int limit) throws Exception {
////        return iotCrawlerClient.getSensors(offset, limit);
////    }
//
//    @Override
//    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getSensors(query, offset, limit);
//    }
//
////    @Override
////    public List<SosaPlatform> getPlatforms(int offset, int limit) throws Exception {
////        return iotCrawlerClient.getPlatforms(offset, limit);
////    }
//
//    @Override
//    public List<Platform> getPlatforms(String query, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getPlatforms(query,offset, limit);
//    }
//
////    @Override
////    public List<ObservableProperty> getObservableProperties(int offset, int limit) throws Exception {
////        return iotCrawlerClient.getObservableProperties(offset, limit);
////    }
//
//    @Override
//    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getObservableProperties(query, offset, limit);
//    }
//
//
////    @Override
////    public List<EntityLD> getEntities(String entityType, int limit) throws Exception {
////        return iotCrawlerClient.getEntities(entityType, limit);
////    }
//
//    @Override
//    public List<String> getEntityURIs(String query, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getEntityURIs(query, offset, limit);
//    }
//
////    public List<StreamObservation> getObservations() throws Exception {
////        return iotCrawlerClient.getObservations();
////    }
//
//
//    public List<StreamObservation> getObservations(String streamId, int offset, int limit) throws Exception {
//        return iotCrawlerClient.getObservations(streamId, offset, limit);
//    }
//
//    @Override
//    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception{
//        return iotCrawlerClient.pushObservationsToBroker(observations);
//    }
//
//    public void pushObservation(StreamObservation observation) throws Exception{
//        iotCrawlerClient.pushObservationsToBroker(new ArrayList<StreamObservation>(){{ add(observation); }});
//    }
//
//    //@Override
////    public void subscribeTo(IoTStream ioTStream, String[] attributes, String referenceUrl) {
////
////    }
//
//    public String subscribeTo(String streamURI, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {
//        return iotCrawlerClient.subscribeTo(streamURI, attributes, notifyConditions, restriction, onChange);
//    }
//
//    public void close() throws IOException {
//
//    }
//}
