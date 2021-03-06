//package com.agtinternational.iotcrawler.orchestrator;
//
///*-
// * #%L
// * orchestrator
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
//
//import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
//import com.agtinternational.iotcrawler.fiware.models.EntityLD;
////import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreMDRClient;
//import com.agtinternational.iotcrawler.core.models.*;
//import org.apache.jena.vocabulary.RDFS;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.Assert;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.function.Function;
//
//import static com.agtinternational.iotcrawler.core.Constants.CUT_TYPE_URIS;
//import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
//import static com.agtinternational.iotcrawler.orchestrator.Constants.HTTP_REFERENCE_URL;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class OrchestratorTests {
//
//    protected Logger LOGGER = LoggerFactory.getLogger(OrchestratorTests.class);
//
//    protected Orchestrator orchestrator;
//    Boolean cutURIs;
//    NgsiLDClient ngsiLDClient;
//
//    @Before
//    public void init(){
//        EnvVariablesSetter.init();
//
//        cutURIs = (System.getenv().containsKey(CUT_TYPE_URIS)?Boolean.parseBoolean(System.getenv(CUT_TYPE_URIS)):false);
//        if(orchestrator ==null)
//            orchestrator = new Orchestrator(cutURIs);
//
//        try {
//            orchestrator.init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ngsiLDClient = new NgsiLDClient(System.getenv(NGSILD_BROKER_URL));
//    }
//
//    @Test
//    @Order(2)
//    public void registerStreamTest() throws Exception {
//        LOGGER.info("registerStreamTest()");
//        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//
////        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
////        ioTStream.setType(IoTStream.getTypeUri());
//
//        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/EntityFromBroker.json"));
//        //EntityLD entityLD = EntityLD.fromJsonString(new String(iotStreamModelJson));
//        //IoTStream ioTStream = IoTStream.fromEntity(entityLD);
//
//        //ioTStream.addProperty(RDFS.label, "label1");
//
//        String label = "TestStream_"+System.currentTimeMillis();
//        IoTStream stream1 = new IoTStream("http://purl.org/iot/ontology/iot-stream#"+label, label);
//
//
//        EntityLD entityLD = stream1.toEntityLD(cutURIs);
//        entityLD.setContext(null);
//        boolean result = ngsiLDClient.addEntitySync(entityLD);
//
//        Assert.isTrue(result);
//        LOGGER.info("Stream was registered");
//    }
//
//    @Ignore
//    @Test
//    public void deleteStream() throws Exception {
//        boolean success = ngsiLDClient.deleteEntitySync("test:testUri2");
//        if(success)
//            org.junit.Assert.assertTrue("Entity deleted", true);
//        else
//            org.junit.Assert.fail("Failed to delete entity");
//
//        LOGGER.info("Entity deleted");
//    }
//
////    @Test
////    @Order(3)
////    public void getStreamsTest() throws Exception {
////        LOGGER.info("getAllStreamsTest()");
////
////        List<IoTStream> streams = orchestrator.getStreams(null, null, 0,0);
////        Assert.notNull(streams);
////
////        LOGGER.info(streams.size()+" streams returned");
////    }
//
//
//    @Test
//    @Order(4)
//    public void getRankedStreamsTest() throws Exception {
//        LOGGER.info("getAllStreamsTest()");
//        //List<IoTStream> streams = orchestrator.getStreams(null,0,0);
//        Map<String, Number> ranking = new HashMap<>();
//        ranking.put("completeness", 0.4);
//        ranking.put("timeliness", 0);
//        ranking.put("plausibility", 0);
//        ranking.put("artificiality", 0.6);
//        ranking.put("concordance", 0);
//
//        List<IoTStream> streams = orchestrator.getStreams(null, ranking, 0,0);
//        Assert.notNull(streams);
//
//        LOGGER.info(streams.size()+" streams returned");
//    }
//
//
////    //@Ignore
////    @Test
////    @Order(6)
////    public void getEntitiesTest() throws Exception {
////        LOGGER.info("getEntitiesTest()");
////        List<EntityLD>  entities = orchestrator.getEntities(IoTStream.getTypeUri(), null, null, 0,0);
////        Assert.notNull(entities);
////        LOGGER.info(entities.size()+" entities returned");
////    }
//
//
//
//    //@Ignore
//    @Test
//    @Order(7)
//    public void getStreamByIdTest() throws Exception {
//        LOGGER.info("getStreamByIdTest()");
////        FilteringSelector selector=new FilteringSelector.Builder()
////        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
////        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
////        .subject("urn:ngsi-ld:Vehicle:A188")
////        .build();
//
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//
//        IoTStream stream = orchestrator.getStreamById(ioTStream.getURI());
//        //List<IoTStream> streams = orchestrator.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
//        Assert.notNull(stream);
//        LOGGER.info(stream.getURI()+" stream returned");
//    }
//
//    @Ignore
//    @Test
//    @Order(8)
//    public void getEntityByIdTest() throws Exception {
//        LOGGER.info("getEntityByIdTest()");
////        FilteringSelector selector=new FilteringSelector.Builder()
////        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
////        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
////        .subject("urn:ngsi-ld:Vehicle:A188")
////        .build();
//
//        String id = "urn:ngsi-ld:MultiSensor_AEON_Labs_ZW100_MultiSensor_6";
//
//        EntityLD entity = orchestrator.getEntityById(id);
//        //List<Sensor> sensors = client.getEntitiesById(ids, Sensor.class);
//
//        Assert.notNull(entity );
//        LOGGER.info(entity.getId()+" entities returned");
//    }
//
//
//    @Test
//    @Order(9)
//    public void getAllSensorsTest() throws Exception {
//        LOGGER.info("getAllSensorsTest()");
//        List<Sensor> sensors = orchestrator.getSensors(null,0,0);
//        Assert.notNull(sensors);
//        LOGGER.info(sensors.size()+" sensors returned");
//    }
//
//
//    @Test
//    @Order(10)
//    public void getAllPlatformsTest() throws Exception {
//        LOGGER.info("getAllPlatformsTest()");
//        List<Platform> platforms = orchestrator.getPlatforms(null,0,0);
//        Assert.notNull(platforms);
//        LOGGER.info(platforms.size()+" platforms returned");
//    }
//
//
//    @Test
//    @Order(11)
//    public void getAllObservablePropertiesTest() throws Exception {
//        LOGGER.info("getAllObservablePropertiesTest()");
//        Map<String,Object> query = new HashMap<>();
//        query.put(RDFS.label.getURI(), "Motion");
//
//        List<ObservableProperty> items = orchestrator.getObservableProperties(query,0,0);
//        Assert.notNull(items);
//        LOGGER.info(items.size()+" ObservableProperties returned");
//    }
//
//    @Ignore
//    @Test
//    @Order(12)
//    public void getByQueryTest() throws Exception {
//        LOGGER.info("getByQueryTest()");
//        //List<EntityLD> streams = orchestrator.getEntities("Vehicle", "brandName.value=Mercedes");
//        Map<String,Object> query = new HashMap<>();
//        query.put("sosa:madeBySensor", "iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay");
//        query.put("http://www.w3.org/2000/01/rdf-schema#label","iotc:Stream_AEON+Labs+ZW100+MultiSensor+6_Brightness");
//
//        List<IoTStream> streams = orchestrator.getStreams(query, null, 0,0);
//        String abc="213";
//    }
//
//    @Test
//    @Order(13)
//    @Ignore
//    public void getCustomEntityTest() throws Exception {
//        LOGGER.info("getCustomEntityTest()");
//        //String query = "{'sosa:madeBySensor' : ['iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature']}";
//        Map<String, Object> query = new HashMap<>();
//        query.put("sosa:madeBySensor", "iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay");
//                //"{\"sosa:madeBySensor\":[\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay\",\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature\"]}";
//        List<EntityLD> models = orchestrator.getEntities(IoTStream.getTypeUri(), query, null, 0,0);
//        String abc="213";
//    }
//
//
//
//
//    @Order(14)
//    @Test
//    @Ignore
//    public void subscribeTest() throws Exception {
//        LOGGER.info("subscribeTest()");
//
//        String streamId = "urn:household1:stateObservation";
//        String referenceURL = System.getenv(HTTP_REFERENCE_URL);
//        String subscriptionId = orchestrator.subscribeTo(streamId,  new Function<StreamObservation, Void>() {
//            @Override
//            public Void apply(StreamObservation streamObservation) {
//                return null;
//            }
//        });
//        org.junit.Assert.assertNotNull(subscriptionId);
//        LOGGER.info("Subscription created");
//    }
//
//
////    @Ignore
////    @Test
////    public void subscribeMultipleTest() throws Exception {
////        LOGGER.info("subscribeMultipleTest()");
////        List<IoTStream> streams = client.getStreams(null, null,0,0);
////        for(IoTStream stream : streams) {
////            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
////            String[] attributes = new String[]{
////                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value",
////                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#state"
////            };
////
////            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
////            client.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
////                    new Function<StreamObservation, Void>() {
////                        @Override
////                        public Void apply(StreamObservation streamObservation) {
////                            Map<String, List<Object>> properties = streamObservation.getProperties();
////                            return null;
////                        }
////                    });
////            String abc = "123";
////        }
////        // orchestrator.run() will hang up test execution
////        //orchestrator.run();
////    }
//
////    @Ignore
////    @Test
////    public void enrichIoTStreamWithAttributes() throws Exception {
////        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
////        // byte[] iotObservationModel = Files.readAllBytes(Paths.get("Observation.json"));
////        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
////        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
////        Model iotSteamModel = ioTStream.getModel();
////        Resource iotStreamResource = iotSteamModel.listResourcesWithProperty(RDF.type).next();
////
////        Property hasAttributeProperty = iotSteamModel.createProperty(iotcNS+"hasAttribute");
////        List<StreamObservation> list = orchestrator.getObservations(1);
////        Model observationModel = list.get(0).getModel();
////        Resource observationResource = observationModel.listResourcesWithProperty(RDF.type).next();
////        StmtIterator iterator = observationResource.listProperties();
////        while (iterator.hasNext()){
////            Statement statement = iterator.next();
////            iotSteamModel.add(iotStreamResource, hasAttributeProperty, statement.getPredicate());
////        }
////
////    }
//
//
////    @Order(15)
////    @Test
////    @Ignore
////    public void pushObservationsTest() throws Exception {
////        LOGGER.info("pushObservationsTest()");
////        byte[] model = Files.readAllBytes(Paths.get("samples/Observation.json"));
////
////        StreamObservation streamObservation = StreamObservation.fromJson(model);
////        client.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{ streamObservation }));
////        Assert.isTrue(true);
////    }
//
//    @Order(15)
//    @Test
//    @Ignore
//    public void getObservationsTest() throws Exception {
//        LOGGER.info("getObservationsTest()");
//        //"iotc:Stream_Z-Wave+Node+002%3A+FGWP102+Metered+Wall+Plug+Switch_Alarm+%28power%29"
//        List<StreamObservation> list = orchestrator.getObservations(null,0,1);
//        //List<StreamObservation> list = orchestrator.getObservations(".*",1);
//        String abc = "abc";
//        Assert.isTrue(true);
//    }
//
//
////    @Ignore
////    @Test
////    public void copyStreamsFromTripleStoreToMDR() throws Exception {
////
////        AbstractMetadataClient metadataClient = new TripleStoreMDRClient();
////        List<IoTStream> streams = metadataClient.getEntitiesAsType(IoTStream.class, 0);
////
////        AbstractMetadataClient metadataClient2 = new NgsiLD_MdrClient();
////        for(IoTStream stream: streams) {
////
////            metadataClient2.registerEntity(stream);
////        }
////
////        //String abc="123";
////    }
//
////    @Ignore
////    @Test
////    public void pushMeasumentsToBroker() throws Exception {
////        List<ContextElement> contextElements = new ArrayList<>();
////        List<StreamObservation> list = rpcClient.;
////        rpcClient.pushObservationsToIoTBroker(list);
////    }
//
//
//}
//
