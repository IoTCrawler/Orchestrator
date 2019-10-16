//package com.agtinternational.iotcrawler.orchestrator;
//
//import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreMDRClient;
//import com.agtinternational.iotcrawler.core.models.*;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//
//public class TripleStoreMDRClientTest extends EnvVariablesSetter{
//
//    TripleStoreMDRClient tripleStoreMDRClient;
//
//    @Before
//    public void init(){
//        super.init();
//        tripleStoreMDRClient = new TripleStoreMDRClient();
//    }
//
//    @Ignore
//    @Test
//    public void getStreamsTest() throws Exception {
//        //tripleStoreClient.getAllDevices();
//        //List<String> list = tripleStoreClient.getSensors();
//        List<IoTStream> list = tripleStoreMDRClient.getEntitiesAsType(IoTStream.class, 0);
//        //List<String> list = tripleStoreClient.listPlatforms();
//        String abc = "abc";
//    }
//
//    @Ignore
//    @Test
//    public void getStreamsWithQueryTest() throws Exception {
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        List<IoTStream> list = tripleStoreMDRClient.getEntitiesAsType(IoTStream.class, "SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+ioTStream.getURI()+">) . } ");
//        //List<String> list = tripleStoreClient.listPlatforms();
//        String abc = "abc";
//    }
//
//
//
//    @Ignore
//    @Test
//    public void getPlatformsTest() throws Exception {
//        //tripleStoreClient.getAllDevices();
//        //List<String> list = tripleStoreClient.getSensors();
//        List<IoTPlatform> list = tripleStoreMDRClient.getEntitiesAsType(IoTPlatform.class, 0);
//        //List<String> list = tripleStoreClient.listPlatforms();
//        String abc = "abc";
//    }
//
//
//
//    @Ignore
//    @Test
//    public void registerEntityTest() throws Exception {
////        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
////        // byte[] iotObservationModel = Files.readAllBytes(Paths.get("Observation.json"));
////        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);
//        int hash = Math.abs("Unknown ZigBee Device 24FD5B000109FA59--".hashCode());
//        IoTStream iotStream = new IoTStream("http://purl.org/iot/ontology/iot-stream#Stream_"+hash, "Stream1");
//        //iotStream.addProperty(iotcNS + "generatedBy", sensor.getResource());
//
//        tripleStoreMDRClient.registerEntity(iotStream);
//        //List<String> list = tripleStoreClient.listPlatforms();
//        String abc = "abc";
//    }
//
//
////    @Ignore
////    @Test
////    public void listPlatforms() throws Exception {
////        List<String> list = tripleStoreClient.;
////        String abc = "abc";
////    }
//
////    @Ignore
////    @Test
////    public void getSensorsTest() throws Exception {
////        List<IoTPlatform> list = tripleStoreMDRClient.getSensors();
////        String abc = "abc";
////    }
//
//
//
//}
