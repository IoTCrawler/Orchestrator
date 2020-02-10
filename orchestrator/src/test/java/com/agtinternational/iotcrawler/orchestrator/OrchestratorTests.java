package com.agtinternational.iotcrawler.orchestrator;

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


import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
//import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreMDRClient;
import com.agtinternational.iotcrawler.core.models.*;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrchestratorTests /*extends EnvVariablesSetter*/ {

    private Logger LOGGER = LoggerFactory.getLogger(OrchestratorTests.class);


    protected IotCrawlerClient orchestatorClient;

    @Before
    public void init(){
        EnvVariablesSetter.init();
        orchestatorClient = new Orchestrator();
        try {
            orchestatorClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void orchestratorTest() throws Exception {
        LOGGER.info("orchestratorTest()");
        orchestatorClient.run();

        String test = "123";
    }


    @Test
    @Order(2)
    @Ignore
    public void registerStreamTest() throws Exception {
        LOGGER.info("registerStreamTest()");
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));

//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        ioTStream.setType(IoTStream.getTypeUri());

        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/EntityFromBroker.json"));
        //EntityLD entityLD = EntityLD.fromJsonString(new String(iotStreamModelJson));
        //IoTStream ioTStream = IoTStream.fromEntity(entityLD);

        //ioTStream.addProperty(RDFS.label, "label1");

        String label = "TestStream_"+System.currentTimeMillis();
        IoTStream stream1 = new IoTStream("http://purl.org/iot/ontology/iot-stream#"+label, label);

        Boolean result = orchestatorClient.registerStream(stream1);
        Assert.isTrue(result);
        LOGGER.info("Stream was registered");
    }


    @Test
    @Order(3)
    public void getStreamsTest() throws Exception {
        LOGGER.info("getAllStreamsTest()");

        List<IoTStream> streams = orchestatorClient.getStreams(null, null, 0,1);
        Assert.notNull(streams);

        LOGGER.info(streams.size()+" streams returned");
    }


    @Test
    @Order(4)
    public void getRankedStreamsTest() throws Exception {
        LOGGER.info("getAllStreamsTest()");
        //List<IoTStream> streams = orchestrator.getStreams(null,0,0);
        Map<String, Number> ranking = new HashMap<>();
        ranking.put("completeness", 0.4);
        ranking.put("timeliness", 0);
        ranking.put("plausibility", 0);
        ranking.put("artificiality", 0.6);
        ranking.put("concordance", 0);

        List<IoTStream> streams = orchestatorClient.getStreams(null, ranking, 0,0);
        Assert.notNull(streams);

        LOGGER.info(streams.size()+" streams returned");
    }

    @Test
    @Order(5)
    @Ignore
    public void registerEntitiesTest() throws Exception {
        LOGGER.info("registerEntityTest()");
        String[] entityTypes = new String[]{ "IoTStream", "Sensor", "Platform" };
        for(String type: entityTypes) {
            byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/"+type+".json"));
            RDFModel entity = RDFModel.fromJson(iotStreamModelJson);
            Boolean result = orchestatorClient.registerEntity(entity);

            Assert.isTrue(result);
            LOGGER.info("Entity {} was registered", type);
        }
    }


    //@Ignore
    @Test
    @Order(6)
    public void getEntitiesTest() throws Exception {
        LOGGER.info("getEntitiesTest()");
        //List<EntityLD>  streams = orchestrator.getEntities(IoTStream.getTypeUri(), 0);
        List<EntityLD>  streams = orchestatorClient.getEntities(".*", null, null, 0,0);
        Assert.notNull(streams);
        LOGGER.info(streams.size()+" streams returned");
    }



    //@Ignore
    @Test
    @Order(7)
    public void getStreamByIdTest() throws Exception {
        LOGGER.info("getStreamByIdTest()");
//        FilteringSelector selector=new FilteringSelector.Builder()
//        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
//        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
//        .subject("urn:ngsi-ld:Vehicle:A188")
//        .build();

        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);

        List<IoTStream> streams = orchestatorClient.getStreamsById(new String[]{ioTStream.getURI()});
        //List<IoTStream> streams = orchestrator.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
        Assert.notNull(streams);
        LOGGER.info(streams.size()+" streams returned");
    }

    @Ignore
    @Test
    @Order(8)
    public void getEntityByIdTest() throws Exception {
        LOGGER.info("getEntityByIdTest()");
//        FilteringSelector selector=new FilteringSelector.Builder()
//        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
//        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
//        .subject("urn:ngsi-ld:Vehicle:A188")
//        .build();

        String[] ids = new String[]{"iotc:Sensor_FIBARO+System+FGWPE%2FF+Wall+Plug+Gen5_CurrentEnergyUse"};

        List<EntityLD> entities = orchestatorClient.getEntitiesById(ids, Sensor.getTypeUri());
        List<Sensor> sensors = orchestatorClient.getEntitiesById(ids, Sensor.class);

        Assert.notNull(entities );
        LOGGER.info(entities.size()+" entities returned");
    }


    @Test
    @Order(9)
    public void getAllSensorsTest() throws Exception {
        LOGGER.info("getAllSensorsTest()");
        List<Sensor> sensors = orchestatorClient.getSensors(null,0,0);
        Assert.notNull(sensors);
        LOGGER.info(sensors.size()+" sensors returned");
    }


    @Test
    @Order(10)
    public void getAllPlatformsTest() throws Exception {
        LOGGER.info("getAllPlatformsTest()");
        List<Platform> platforms = orchestatorClient.getPlatforms(null,0,0);
        Assert.notNull(platforms);
        LOGGER.info(platforms.size()+" platforms returned");
    }


    @Test
    @Order(11)
    public void getAllObservablePropertiesTest() throws Exception {
        LOGGER.info("getAllObservablePropertiesTest()");
        JsonObject query = new JsonObject();
        query.addProperty(RDFS.label.getURI(), "Motion");

        List<ObservableProperty> items = orchestatorClient.getObservableProperties(query.toString(),0,0);
        Assert.notNull(items);
        LOGGER.info(items.size()+" ObservableProperties returned");
    }

    @Ignore
    @Test
    @Order(12)
    public void getByQueryTest() throws Exception {
        LOGGER.info("getStreamByCustomQueryTest()");
        //List<EntityLD> streams = orchestrator.getEntities("Vehicle", "brandName.value=Mercedes");
        JsonObject query = new JsonObject();
        query.addProperty("sosa:madeBySensor", "iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay");
        query.addProperty("http://www.w3.org/2000/01/rdf-schema#label","iotc:Stream_AEON+Labs+ZW100+MultiSensor+6_Brightness");

        List<IoTStream> streams = orchestatorClient.getStreams(query.toString(), null, 0,0);
        String abc="213";
    }

    @Test
    @Order(13)
    @Ignore
    public void getCustomEntityTest() throws Exception {
        LOGGER.info("getCustomEntityTest()");
        //String query = "{'sosa:madeBySensor' : ['iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature']}";
        String query = "{\"sosa:madeBySensor\":[\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_MotionAlarmCancelationDelay\",\"iotc:Sensor_AEON+Labs+ZW100+MultiSensor+6_Temperature\"]}";
        List<EntityLD> models = orchestatorClient.getEntities(IoTStream.getTypeUri(), query, null, 0,0);
        String abc="213";
    }




    @Order(14)
    @Test
    @Ignore
    public void subscribeToIoTBrokerTest() throws Exception {
        LOGGER.info("subscribeTest()");
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);

        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
        String entityUri = ioTStream.getURI();

        String[] attributes = new String[]{ "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value" };
        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        orchestatorClient.subscribeTo(entityUri, attributes, Arrays.asList(new NotifyCondition[]{ notifyCondition }), new Restriction(),
             new Function<StreamObservation, Void>() {
            @Override
            public Void apply(StreamObservation streamObservation){
                Map<String, List<Object>> properties = streamObservation.getProperties();
                return null;
            }
        });
        // orchestrator.run() will hang up test execution
        //orchestrator.run();
    }




    @Ignore
    @Test
    public void subscribeMultipleTest() throws Exception {
        LOGGER.info("subscribeMultipleTest()");
        List<IoTStream> streams = orchestatorClient.getStreams(null, null,0,0);
        for(IoTStream stream : streams) {
            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
            String[] attributes = new String[]{
                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value",
                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#state"
            };

            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
            orchestatorClient.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
                    new Function<StreamObservation, Void>() {
                        @Override
                        public Void apply(StreamObservation streamObservation) {
                            Map<String, List<Object>> properties = streamObservation.getProperties();
                            return null;
                        }
                    });
            String abc = "123";
        }
        // orchestrator.run() will hang up test execution
        //orchestrator.run();
    }

//    @Ignore
//    @Test
//    public void enrichIoTStreamWithAttributes() throws Exception {
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        // byte[] iotObservationModel = Files.readAllBytes(Paths.get("Observation.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
//        Model iotSteamModel = ioTStream.getModel();
//        Resource iotStreamResource = iotSteamModel.listResourcesWithProperty(RDF.type).next();
//
//        Property hasAttributeProperty = iotSteamModel.createProperty(iotcNS+"hasAttribute");
//        List<StreamObservation> list = orchestrator.getObservations(1);
//        Model observationModel = list.get(0).getModel();
//        Resource observationResource = observationModel.listResourcesWithProperty(RDF.type).next();
//        StmtIterator iterator = observationResource.listProperties();
//        while (iterator.hasNext()){
//            Statement statement = iterator.next();
//            iotSteamModel.add(iotStreamResource, hasAttributeProperty, statement.getPredicate());
//        }
//
//    }


    @Order(15)
    @Test
    @Ignore
    public void pushObservationsTest() throws Exception {
        LOGGER.info("pushObservationsTest()");
        byte[] model = Files.readAllBytes(Paths.get("samples/Observation.json"));

        StreamObservation streamObservation = StreamObservation.fromJson(model);
        orchestatorClient.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{ streamObservation }));
        Assert.isTrue(true);
    }

    @Order(15)
    @Test
    @Ignore
    public void getObservationsTest() throws Exception {
        LOGGER.info("getObservationsTest()");
        List<StreamObservation> list = orchestatorClient.getObservations("iotc:Stream_Z-Wave+Node+002%3A+FGWP102+Metered+Wall+Plug+Switch_Alarm+%28power%29",0,1);
        //List<StreamObservation> list = orchestrator.getObservations(".*",1);
        String abc = "abc";
        Assert.isTrue(true);
    }


//    @Ignore
//    @Test
//    public void copyStreamsFromTripleStoreToMDR() throws Exception {
//
//        AbstractMetadataClient metadataClient = new TripleStoreMDRClient();
//        List<IoTStream> streams = metadataClient.getEntitiesAsType(IoTStream.class, 0);
//
//        AbstractMetadataClient metadataClient2 = new NgsiLD_MdrClient();
//        for(IoTStream stream: streams) {
//
//            metadataClient2.registerEntity(stream);
//        }
//
//        //String abc="123";
//    }

//    @Ignore
//    @Test
//    public void pushMeasumentsToBroker() throws Exception {
//        List<ContextElement> contextElements = new ArrayList<>();
//        List<StreamObservation> list = rpcClient.;
//        rpcClient.pushObservationsToIoTBroker(list);
//    }


}

