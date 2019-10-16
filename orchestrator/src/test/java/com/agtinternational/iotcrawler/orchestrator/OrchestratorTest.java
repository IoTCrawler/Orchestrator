package com.agtinternational.iotcrawler.orchestrator;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.orchestrator.clients.NgsiLD_MdrClient;
//import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreMDRClient;
import com.agtinternational.iotcrawler.core.models.*;
import org.apache.commons.lang3.tuple.Pair;
import com.agtinternational.iotcrawler.orchestrator.clients.AbstractMetadataClient;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.iotcNS;
import static com.agtinternational.iotcrawler.orchestrator.Constants.*;

public class OrchestratorTest extends EnvVariablesSetter {

    Orchestrator orchestrator;

    @Before
    public void init(){
        super.init();
        orchestrator = new Orchestrator();
        try {
            orchestrator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void orchestratorTest() throws Exception {
        orchestrator.run();
        String abc = "123";
    }


    @Test
    public void registerStreamTest() throws Exception {
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        //IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);

        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/EntityFromBroker.json"));
        EntityLD entityLD = EntityLD.fromJsonString(new String(iotStreamModelJson));
        IoTStream ioTStream = IoTStream.fromEntity(entityLD);

        //ioTStream.addProperty(RDFS.label, "label1");
        orchestrator.registerStream(ioTStream);
        Assert.isTrue(true);
    }

    @Test
    public void registerEntityTest() throws Exception {
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/Platform.json"));
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/Property.json"));
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        RDFModel entity = RDFModel.fromJson(iotStreamModelJson);
        //ioTStream.addProperty(RDFS.label, "label1");
        orchestrator.registerEntity(entity);
        Assert.isTrue(true);
    }

    @Ignore
    @Test
    public void getAllStreamsTest() throws Exception {

        List<IoTStream> streams = orchestrator.getStreams(0);
        Assert.notNull(streams);

        System.out.println(streams.size()+" streams returned");
    }

    @Ignore
    @Test
    public void getEntitiesTest() throws Exception {

        //List<EntityLD>  streams = orchestrator.getEntities(IoTStream.getTypeUri(), 0);
        List<EntityLD>  streams = orchestrator.getEntities(".*", 0);
        Assert.notNull(streams);

        System.out.println(streams.size()+" streams returned");
    }



    @Ignore
    @Test
    public void getStreamByIdTest() throws Exception {

//        FilteringSelector selector=new FilteringSelector.Builder()
//        //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
//        //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
//        .subject("urn:ngsi-ld:Vehicle:A188")
//        .build();

        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);

        List<IoTStream> streams = orchestrator.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
        Assert.notNull(streams);
        System.out.println(streams.size()+" streams returned");
    }

    @Ignore
    @Test
    public void getAllSensorsTest() throws Exception {

        List<Sensor> sensors = orchestrator.getSensors(0);
        Assert.notNull(sensors);
        System.out.println(sensors.size()+" sensors returned");
    }

    @Ignore
    @Test
    public void getAllPlatformsTest() throws Exception {

        List<IoTPlatform> platforms = orchestrator.getPlatforms(0);
        Assert.notNull(platforms);
        System.out.println(platforms.size()+" platforms returned");
    }

    @Ignore
    @Test
    public void getAllObservablePropertiesTest() throws Exception {

        List<ObservableProperty> items = orchestrator.getObservableProperties(0);
        Assert.notNull(items);
        System.out.println(items.size()+" ObservableProperties returned");
    }

    @Ignore
    @Test
    public void getStreamByCustomQueryTest() {
        List<IoTStream> streams = orchestrator.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?p=<http://www.w3.org/ns/sosa/madeBySensor> && ?o=<http://purl.org/iot/ontology/iot-stream#Sensor_FIBARO+Wall+plug+living+room_CurrentEnergyUse>) }");
        String abc="213";
    }

    @Ignore
    @Test
    public void getCustomEntityTest() throws Exception {
        List<EntityLD> models = orchestrator.getEntities("http://www.agtinternational.com/ontologies/SmartHome#IoTDevice", 0);
        String abc="213";
    }




    @Ignore
    @Test
    public void subscribeTest() throws Exception {
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);

        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
        String entityUri = ioTStream.getURI();

        String[] attributes = new String[]{ "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value" };
        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        orchestrator.subscribeTo(entityUri, attributes, Arrays.asList(new NotifyCondition[]{ notifyCondition }), new Restriction(),
             new Function<StreamObservation, Void>() {
            @Override
            public Void apply(StreamObservation streamObservation){
                Map<String, List<Object>> properties = streamObservation.getProperties();
                return null;
            }
        });
        orchestrator.run();
    }




    @Ignore
    @Test
    public void subscribeMultipleTest() throws Exception {

        List<IoTStream> streams = orchestrator.getStreams(0);
        for(IoTStream stream : streams) {
            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
            String[] attributes = new String[]{
                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value",
                    "http://www.agtinternational.com/iotcrawler/ontologies/iotc#state"
            };

            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
            orchestrator.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
                    new Function<StreamObservation, Void>() {
                        @Override
                        public Void apply(StreamObservation streamObservation) {
                            Map<String, List<Object>> properties = streamObservation.getProperties();
                            return null;
                        }
                    });
            String abc = "123";
        }
        orchestrator.run();
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


    @Ignore
    @Test
    public void pushObservationsTest() throws Exception {
        byte[] model = Files.readAllBytes(Paths.get("samples/Observation.json"));

        StreamObservation streamObservation = StreamObservation.fromJson(model);
        orchestrator.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{ streamObservation }));
        String abc = "abc";
    }

    @Ignore
    @Test
    public void getObservationsTest() throws Exception {
        List<StreamObservation> list = orchestrator.getObservations("iotc:Stream_Z-Wave+Node+002%3A+FGWP102+Metered+Wall+Plug+Switch_Alarm+%28power%29",1);
        //List<StreamObservation> list = orchestrator.getObservations(".*",1);
        String abc = "abc";
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

