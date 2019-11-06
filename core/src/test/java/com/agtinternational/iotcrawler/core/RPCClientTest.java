package com.agtinternational.iotcrawler.core;

import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
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

public class RPCClientTest extends EnvVariablesSetter {

    OrchestratorRPCClient rpcClient;

    @Before
    public void init(){
        super.init();

        rpcClient = new OrchestratorRPCClient();
        try {
            rpcClient.init();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void registerStreamTest() throws Exception {
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(new String(iotStreamModelJson));
        Boolean result = rpcClient.registerEntity(ioTStream);
        Assert.isTrue(result);
    }

    @Test
    public void registerEntityTest() throws Exception {
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/Platform.json"));
        //byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        RDFModel entity = RDFModel.fromJson(new String(iotStreamModelJson));
        //ioTStream.addProperty(RDFS.label, "label1");
        Boolean result = rpcClient.registerEntity(entity);
        Assert.isTrue(result);
    }

    @Ignore
    @Test
    public void getAllStreamsTest() throws Exception {

        List<IoTStream> streams = rpcClient.getStreams(0);
        //streams.get(0).getSensorUri();
        Assert.notNull(streams);
        System.out.println(streams.size()+" streams returned");
    }

    @Ignore
    @Test
    public void getEntitiesTest() throws Exception {

        List<EntityLD> streams = rpcClient.getEntities(IoTStream.getTypeUri(), 0);
        //streams.get(0).getSensorUri();
        Assert.notNull(streams);
        System.out.println(streams.size()+" enitites returned");
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
        IoTStream ioTStream = IoTStream.fromJson(new String(iotStreamModelJson));

        List<IoTStream> streams = rpcClient.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
        Assert.notNull(streams);
        System.out.println(streams.size()+" streams returned");
    }

    @Ignore
    @Test
    public void getAllSensorsTest() throws Exception {

        List<Sensor> sensors = rpcClient.getSensors(0);
        Assert.notNull(sensors);
        System.out.println(sensors.size()+" sensors returned");
    }

    @Ignore
    @Test
    public void getAllPlatformsTest() throws Exception {

        List<SosaPlatform> platforms = rpcClient.getPlatforms(0);
        Assert.notNull(platforms);
        System.out.println(platforms.size()+" platforms returned");
    }

    @Ignore
    @Test
    public void getAllObservablePropertiesTest() throws Exception {

        List<ObservableProperty> items = rpcClient.getObservableProperties(0);
        Assert.notNull(items);
        System.out.println(items.size()+" ObservableProperties returned");
    }

    @Ignore
    @Test
    public void getStreamByCustomQueryTest() throws Exception {
        List<IoTStream> streams = rpcClient.getStreams("SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER(?p=<http://www.w3.org/ns/sosa/madeBySensor> && ?o=<http://purl.org/iot/ontology/iot-stream#Sensor_FIBARO+Wall+plug+living+room_CurrentEnergyUse>) }");
        String abc="213";
    }


    @Ignore
    @Test
    public void subscribeTest() throws Exception {
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(new String(iotStreamModelJson));
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);

        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
        String entityUri = ioTStream.getURI();
        String attributeUri = "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value";
        String[] attributes = new String[]{ attributeUri };
        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        rpcClient.subscribeTo(entityUri, attributes, Arrays.asList(new NotifyCondition[]{ notifyCondition }), new Restriction(),
                new Function<StreamObservation, Void>() {
                    @Override
                    public Void apply(StreamObservation streamObservation){
                        System.out.println(Utils.getFragment(attributeUri)+"="+streamObservation.getProperty(attributeUri));
                        return null;
                    }
                });
        long until = System.currentTimeMillis()+60000;
        while(System.currentTimeMillis()<until)
            Thread.sleep(1000);
    }

    @Ignore
    @Test
    public void subscribeMultipleTest() throws Exception {

        List<IoTStream> streams = rpcClient.getStreams(0);
        for(IoTStream stream : streams) {
            //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
            String[] attributes = new String[]{"http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value"};
            NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
            rpcClient.subscribeTo(stream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(),
                    new Function<StreamObservation, Void>() {
                        @Override
                        public Void apply(StreamObservation streamObservation) {
                            Map<String, List<Object>> attributes = streamObservation.getProperties();
                            return null;
                        }
                    });
            String abc = "123";
        }
        long until = System.currentTimeMillis()+60000;
        while(System.currentTimeMillis()<until)
            Thread.sleep(1000);
    }

    @Ignore
    @Test
    public void enrichIoTStreamWithAttributes() throws Exception {
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        // byte[] iotObservationModel = Files.readAllBytes(Paths.get("Observation.json"));
        IoTStream ioTStream = IoTStream.fromJson(new String(iotStreamModelJson));
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
        Model iotSteamModel = ioTStream.getModel();
        Resource iotStreamResource = iotSteamModel.listResourcesWithProperty(RDF.type).next();

        Property hasAttributeProperty = iotSteamModel.createProperty(iotcNS+"hasAttribute");
        List<StreamObservation> list = rpcClient.getObservations(ioTStream.getURI(), 1);
        Model observationModel = list.get(0).getModel();
        Resource observationResource = observationModel.listResourcesWithProperty(RDF.type).next();
        StmtIterator iterator = observationResource.listProperties();
        while (iterator.hasNext()){
            Statement statement = iterator.next();
            iotSteamModel.add(iotStreamResource, hasAttributeProperty, statement.getPredicate());
        }

    }


    @Ignore
    @Test
    public void pushObservationsTest() throws Exception {
        byte[] model = new byte[0];
        try {
            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamObservation streamObservation = StreamObservation.fromJson(new String(model));
        rpcClient.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{ streamObservation }));
        String abc = "abc";
    }

    @Ignore
    @Test
    public void getObservationsTest() throws Exception {
        List<StreamObservation> list = rpcClient.getObservations("iotc:Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%29",1);
        Map<String, List<Object>> streamObservation = list.get(0).getProperties("http://www.agtinternational.com/ontologies/SmartHome#current_value");
        String abc = "abc";
    }



//    @Ignore
//    @Test
//    public void pushMeasumentsToBroker() throws Exception {
//        List<ContextElement> contextElements = new ArrayList<>();
//        List<StreamObservation> list = rpcClient.;
//        rpcClient.pushObservationsToIoTBroker(list);
//    }

}
