package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.core.OrchestratorRESTClient;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.orchestrator.clients.NgsiLD_MdrClient;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.agtinternational.iotcrawler.orchestrator.Constants.IOTCRAWLER_ORCHESTRATOR_URL;

//public class OrchestratorTestsREST extends EnvVariablesSetter {
public class OrchestratorTestsREST extends OrchestratorTests {

    private Logger LOGGER = LoggerFactory.getLogger(OrchestratorTests.class);

    Thread thread;
    Orchestrator orchestrator;
    //NgsiLD_MdrClient orchestratorRestClient;
    //Semaphore orchestratorStartedMutex;


    @Override
    public void init() {
        EnvVariablesSetter.init();
        orchestatorClient = new OrchestratorRESTClient( System.getenv().get(IOTCRAWLER_ORCHESTRATOR_URL));
//        orchestrator = new Orchestrator();
        try {
//            orchestrator.init();
            orchestatorClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    orchestrator.run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

//    @Test
//    @Order(1)
//    public void startingOrchestatorTest() throws Exception {
//        LOGGER.info("Starting orchestrator");
//        thread.start();
//
//        try {
//            LOGGER.info("Waiting 3 seconds");
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        LOGGER.info("Orchestrator should be started. Starting tests");
//    }

//    @Test
//    @Order(2)
//    public void registerStreamTest() throws Exception {
//        LOGGER.info("registerStreamTest()");
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        ioTStream.setType(IoTStream.getTypeUri());
//
//        //IoTStream stream1 = new IoTStream("http://purl.org/iot/ontology/iot-stream#Stream_FIBARO%2520Wall%2520plug%2520living%2520room_CurrentEnergyUse", "CurrentEnergyUse");
//        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/EntityFromBroker.json"));
//        //EntityLD entityLD = EntityLD.fromJsonString(new String(iotStreamModelJson));
//        //IoTStream ioTStream = IoTStream.fromEntity(entityLD);
//
//        //ioTStream.addProperty(RDFS.label, "label1");
//        Boolean result = orchestratorRestClient.registerEntity(ioTStream);
//        Assert.isTrue(result);
//
//        LOGGER.info("Stream was registered");
//    }
//
//
//    @Test
//    @Order(3)
//    public void getStreamsTest() throws Exception {
//        LOGGER.info("getAllStreamsTest()");
//
//        List<EntityLD> entities = orchestratorRestClient.getEntities(IoTStream.getTypeUri(), null, null, 0, 0);
//        Assert.notNull(entities);
//
//        LOGGER.info(entities.size() + " streams returned");
//    }
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
//        //List<IoTStream> streams = orchestrator.getStreams(null, ranking, 0, 0);
//        List<EntityLD> entities = orchestratorRestClient.getEntities(IoTStream.getTypeUri(), null, ranking, 0, 0);
//        Assert.notNull(entities);
//
//        LOGGER.info(entities.size() + " streams returned");
//    }
//
//    @Ignore
//    @Test
//    @Order(5)
//    public void registerEntitiesTest() throws Exception {
//        LOGGER.info("registerEntityTest()");
//        String[] entityTypes = new String[]{"IoTStream", "Sensor", "Platform"};
//        for (String type : entityTypes) {
//            byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/" + type + ".json"));
//            RDFModel entity = RDFModel.fromJson(iotStreamModelJson);
//            //Boolean result = orchestrator.registerEntity(entity);
//            Boolean result = orchestratorRestClient.registerEntity(entity);
//
//            Assert.isTrue(result);
//            LOGGER.info("Entity {} was registered", type);
//        }
//    }
//
//
//    //@Ignore
//    @Test
//    @Order(6)
//    public void getEntitiesTest() throws Exception {
//        LOGGER.info("getEntitiesTest()");
//        //List<EntityLD>  streams = orchestrator.getEntities(IoTStream.getTypeUri(), 0);
//        //List<EntityLD> streams = orchestrator.getEntities(".*", null, null, 0, 0);
//        List<EntityLD> entities = orchestratorRestClient.getEntities(IoTStream.getTypeUri(), null, null, 0, 0);
//        Assert.notNull(entities);
//        LOGGER.info(entities.size() + " entities returned");
//    }
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
//        List<EntityLD> streams = orchestratorRestClient.getEntitiesById(new String[]{ioTStream.getURI()}, IoTStream.getTypeUri());
//        //List<IoTStream> streams = orchestrator.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
//        Assert.notNull(streams);
//        LOGGER.info(streams.size() + " streams returned");
//    }
//
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
//        String[] ids = new String[]{"iotc:Sensor_FIBARO+System+FGWPE%2FF+Wall+Plug+Gen5_CurrentEnergyUse"};
//
//        List<EntityLD> streams = orchestratorRestClient.getEntitiesById(ids, Sensor.getTypeUri());
//        //List<Sensor> sensors = orchestrator.getEntitiesById(ids, Sensor.class);
//
//        Assert.notNull(streams);
//        LOGGER.info(streams.size() + " entities returned");
//    }
//
//    //@Ignore
//    @Test
//    @Order(9)
//    public void getAllSensorsTest() throws Exception {
//        LOGGER.info("getAllSensorsTest()");
//        List<EntityLD> sensors = orchestratorRestClient.getEntities(Sensor.getTypeUri(), null, null, 0,0);
//        Assert.notNull(sensors);
//        LOGGER.info(sensors.size() + " sensors returned");
//    }
//
//    //@Ignore
//    @Test
//    @Order(10)
//    public void getAllPlatformsTest() throws Exception {
//        LOGGER.info("getAllPlatformsTest()");
//        List<EntityLD> platforms = orchestratorRestClient.getEntities(Platform.getTypeUri(), null, null, 0,0);
//        Assert.notNull(platforms);
//        LOGGER.info(platforms.size() + " platforms returned");
//    }
//
//    //@Ignore
////    @Test
////    public void getAllObservablePropertiesTest() throws Exception {
////        LOGGER.info("getAllObservablePropertiesTest()");
////        JsonObject query = new JsonObject();
////        query.addProperty(RDFS.label.getURI(), "Motion");
////
////        List<ObservableProperty> items = orchestrator.getObservableProperties(query.toString(), 0, 0);
////        Assert.notNull(items);
////        LOGGER.info(items.size() + " ObservableProperties returned");
////    }
//
//    @Ignore
//    @Test
//    @Order(11)
//    public void getByQueryTest() throws Exception {
//        LOGGER.info("getStreamByCustomQueryTest()");
//        //List<EntityLD> streams = orchestrator.getEntities("Vehicle", "brandName.value=Mercedes");
//        JsonObject query = new JsonObject();
//        query.addProperty("sosa:madeBySensor", "iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay");
//        query.addProperty("http://www.w3.org/2000/01/rdf-schema#label", "iotc:Stream_AEON+Labs+ZW100+MultiSensor+6_Brightness");
//
//        //List<IoTStream> streams = orchestrator.getStreams(query.toString(), null, 0, 0);
//        List<EntityLD> platforms = orchestratorRestClient.getEntities(IoTStream.getTypeUri(), query.toString(), null, 0,0);
//        Assert.notNull(platforms);
//        LOGGER.info(platforms.size() + " Streams returned");
//    }
//
////    @Ignore
////    @Test
////    @Order(8)
////    public void getCustomEntityTest() throws Exception {
////        LOGGER.info("getCustomEntityTest()");
////        //String query = "{'sosa:madeBySensor' : ['iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature']}";
////        String query = "{\"sosa:madeBySensor\":[\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay\",\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature\"]}";
////        List<EntityLD> models = orchestrator.getEntities(IoTStream.getTypeUri(), query, null, 0, 0);
////        String abc = "213";
////    }
//
//
//    @Order(12)
//    @Test
//    public void subscribeTest() throws Exception {
//        LOGGER.info("subscribeTest()");
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
//
//        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
//        String entityUri = ioTStream.getURI();
//
//        String[] attributes = new String[]{"http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value"};
//        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
//
////        ngsiLD_mdrClient.subscribeTo(entityUri, attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
////                new Function<StreamObservation, Void>() {
////                    @Override
////                    public Void apply(StreamObservation streamObservation) {
////                        Map<String, List<Object>> properties = streamObservation.getProperties();
////                        return null;
////                    }
////                });
//        // orchestrator.run() will hang up test execution
//        //orchestrator.run();
//    }
//
//
////    @Ignore
////    @Test
////    public void subscribeMultipleTest() throws Exception {
////        LOGGER.info("subscribeMultipleTest()");
////        List<IoTStream> streams = orchestrator.getStreams(null, null, 0, 0);
////        for (IoTStream stream : streams) {
////            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
////            String[] attributes = new String[]{
////                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value",
////                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#state"
////            };
////
////            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
////            orchestrator.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
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
////    @Ignore
////    @Test
////    public void pushObservationsTest() throws Exception {
////        LOGGER.info("pushObservationsTest()");
////        byte[] model = Files.readAllBytes(Paths.get("samples/Observation.json"));
////
////        StreamObservation streamObservation = StreamObservation.fromJson(model);
////        orchestrator.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{streamObservation}));
////        Assert.isTrue(true);
////    }
//
////    @Ignore
////    @Test
////    public void getObservationsTest() throws Exception {
////        LOGGER.info("getObservationsTest()");
////        List<StreamObservation> list = orchestrator.getObservations("iotc:Stream_Z-Wave+Node+002%3A+FGWP102+Metered+Wall+Plug+Switch_Alarm+%28power%29", 1);
////        //List<StreamObservation> list = orchestrator.getObservations(".*",1);
////        String abc = "abc";
////        Assert.isTrue(true);
////    }

//    @Order(99)
//    @Test
//    public void stoppingOrchestatorTest() throws Exception {
//        LOGGER.info("stopping orchestrator");
//        thread.stop();
//    }


}
