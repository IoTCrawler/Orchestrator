//
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
//import com.agtinternational.iotcrawler.orchestrator.clients.IotBrokerDataClient;
//import com.agtinternational.iotcrawler.core.models.*;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.List;
//
//public class IoTBrokerDataClientTest{
//
//    IotBrokerDataClient dataClient;
//    private final static String httpServerUrl = "http://10.67.1.41:3001/notify";
//
//    @Before
//    public void init(){
//        EnvVariablesSetter.init();
//        dataClient = new IotBrokerDataClient();
//    }
//
//    @Ignore
//    @Test
//    public void pushObservationsTest(){
//        byte[] model = new byte[0];
//        try {
//            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        StreamObservation streamObservation = StreamObservation.fromJson(model);
//        dataClient.pushObservations(Arrays.asList(new StreamObservation[]{ streamObservation }));
//        String abc = "abc";
//    }
//
//    @Ignore
//    @Test
//    public void getObservationsTest() throws Exception {
//        byte[] model = new byte[0];
//        try {
//            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        StreamObservation streamObservation = StreamObservation.fromJson(model);
//
//        List<StreamObservation> list = dataClient.getObservations(streamObservation.getURI(),0);
//        String abc = "abc";
//    }
//
//
//
//}
//
