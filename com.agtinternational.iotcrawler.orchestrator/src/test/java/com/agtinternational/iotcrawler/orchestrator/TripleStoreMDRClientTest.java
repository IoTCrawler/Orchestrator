//package com.agtinternational.iotcrawler.orchestrator;

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
