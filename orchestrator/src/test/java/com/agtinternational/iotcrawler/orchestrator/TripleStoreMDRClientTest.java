//package com.agtinternational.iotcrawler.orchestrator;

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
