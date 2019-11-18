package com.agtinternational.iotcrawler.orchestrator;

import com.agtinternational.iotcrawler.core.OrchestratorRPCClient;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.models.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.iotcNS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RPCClientTest extends EnvVariablesSetter {

    private Logger LOGGER = LoggerFactory.getLogger(OrchestratorTest.class);

    OrchestratorRPCClient rpcClient;
    Orchestrator orchestrator;


    @Before
    public void init(){
        super.init();
        //orchestrator = new Orchestrator();
        rpcClient = new OrchestratorRPCClient();
        try {
            if(orchestrator!=null)
                orchestrator.init();
            rpcClient.init();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(orchestrator!=null)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    orchestrator.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    @Order(1)
    @Test
    public void registerStreamTest() throws Exception {
        LOGGER.info("registerStreamTest()");
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
        //ioTStream.addProperty(RDFS.label, "label1");
        Boolean result = rpcClient.registerEntity(ioTStream);
        LOGGER.info("Stream registered");
        Assert.isTrue(result);
    }

    @Order(2)
    @Test
    public void registerEntityTest() throws Exception {
        LOGGER.info("registerEntityTest()");
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/Platform.json"));
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        RDFModel entity = RDFModel.fromJson(iotStreamModelJson);
        //ioTStream.addProperty(RDFS.label, "label1");
        Boolean result = rpcClient.registerEntity(entity);
        Assert.isTrue(result);
    }

    @Order(3)
    @Test
    public void getAllStreamsTest() throws Exception {
        LOGGER.info("getAllStreamsTest()");
        List<IoTStream> streams = rpcClient.getStreams(null,0,0);
        //streams.get(0).getSensorUri();
        LOGGER.info(streams.size()+" streams returned");
        Assert.notNull(streams);

    }

    @Ignore
    @Order(4)
    @Test
    public void getStreamByIdTest() throws Exception {
        LOGGER.info("getStreamByIdTest()");
//        FilteringSelector selector=new FilteringSelector.Builder()
//        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
//        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
//        .subject("urn:ngsi-ld:Vehicle:A188")
//        .build();

//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//
//        List<IoTStream> streams = rpcClient.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ", 0,0);
//        LOGGER.info(streams.size()+" streams returned");
//        Assert.notNull(streams);
    }

    @Order(5)
    @Test
    public void getAllSensorsTest() throws Exception {
        LOGGER.info("getAllSensorsTest()");
        List<Sensor> sensors = rpcClient.getSensors(null,0,0);
        Assert.notNull(sensors);
        LOGGER.info(sensors.size()+" sensors returned");
    }

    @Order(6)
    @Test
    public void getAllPlatformsTest() throws Exception {
        LOGGER.info("getAllPlatformsTest()");
        List<SosaPlatform> platforms = rpcClient.getPlatforms(null,0,0);
        Assert.notNull(platforms);
        LOGGER.info(platforms.size()+" platforms returned");
    }

    @Order(7)
    @Test
    public void getAllObservablePropertiesTest() throws Exception {
        LOGGER.info("getAllObservablePropertiesTest()");
        List<ObservableProperty> items = rpcClient.getObservableProperties(null,0,0);
        Assert.notNull(items);
        LOGGER.info(items.size()+" ObservableProperties returned");
    }

    @Ignore
    @Test
    public void getStreamByCustomQueryTest() throws Exception {
        LOGGER.info("getStreamByCustomQueryTest()");
        //List<IoTStream> streams = rpcClient.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?p=<http://www.w3.org/ns/sosa/madeBySensor> && ?o=<http://purl.org/iot/ontology/iot-stream#Sensor_FIBARO+Wall+plug+living+room_CurrentEnergyUse>) }",0,0);
        //String abc="213";
    }

    @Order(8)
    @Test
    public void subscribeTest() throws Exception {
        LOGGER.info("subscribeTest()");
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);

        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
        String entityUri = ioTStream.getURI();
        String attributeUri = "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value";
        String[] attributes = new String[]{ attributeUri };
        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        String subcriptionId = rpcClient.subscribeTo(entityUri, attributes, Arrays.asList(new NotifyCondition[]{ notifyCondition }), new Restriction(),
                new Function<StreamObservation, Void>() {
                    @Override
                    public Void apply(StreamObservation streamObservation){
                        LOGGER.info(Utils.getFragment(attributeUri)+"="+streamObservation.getAttribute(attributeUri));
                        return null;
                    }
                });

//        long until = System.currentTimeMillis()+60000;
//        while(System.currentTimeMillis()<until)
//            Thread.sleep(1000);
        Assert.notNull(subcriptionId);
    }

    @Ignore
    @Test
    public void subscribeMultipleTest() throws Exception {

        List<IoTStream> streams = rpcClient.getStreams(null,0,0);
        for(IoTStream stream : streams) {
            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
            String[] attributes = new String[]{"http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value"};
            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
            rpcClient.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
                    new Function<StreamObservation, Void>() {
                        @Override
                        public Void apply(StreamObservation streamObservation) {
//                            List<Pair<String, Object>> attributes = streamObservation.getAttributes();
                            return null;
                        }
                    });
            String abc = "123";
        }

        long until = System.currentTimeMillis()+60000;
        while(System.currentTimeMillis()<until)
            Thread.sleep(1000);
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
//        List<StreamObservation> list = rpcClient.getObservations(1);
//        Model observationModel = list.get(0).getModel();
//        Resource observationResource = observationModel.listResourcesWithProperty(RDF.type).next();
//        StmtIterator iterator = observationResource.listProperties();
//        while (iterator.hasNext()){
//            Statement statement = iterator.next();
//            iotSteamModel.add(iotStreamResource, hasAttributeProperty, statement.getPredicate());
//        }
//
//    }

    @Order(9)
    @Test
    public void pushObservationsTest() throws Exception {
        LOGGER.info("pushObservationsTest()");
        byte[] model = new byte[0];
        try {
            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamObservation streamObservation = StreamObservation.fromJson(model);
        rpcClient.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{ streamObservation }));
        String abc = "abc";
    }

    @Order(10)
    @Test
    public void getObservationsTest() throws Exception {
        LOGGER.info("pushObservationsTest()");
        List<StreamObservation> list = rpcClient.getObservations("streamId",0, 1);
        //list.get(0).getModel();
        String abc = "abc";
    }



//    @Ignore
//    @Test
//    public void pushMeasumentsToBroker() throws Exception {
//        List<ContextElement> contextElements = new ArrayList<>();
//        List<StreamObservation> list = rpcClient.;
//        rpcClient.pushObservationsToIoTBroker(list);
//    }

    @After
    public void close() throws IOException {
        if(orchestrator!=null)
            orchestrator.close();
    }
}
