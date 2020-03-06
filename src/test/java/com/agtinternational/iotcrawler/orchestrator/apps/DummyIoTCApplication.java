//package com.agtinternational.iotcrawler.orchestrator.apps;

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
//import com.agtinternational.iotcrawler.core.interfaces.AbstractIoTCrawlerApp;
//import com.agtinternational.iotcrawler.core.models.*;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class DummyIoTCApplication extends AbstractIoTCrawlerApp {
//
//    @Override
//    public void init() throws Exception {
//
//        List<IoTStream> ioTStream = getStreams(1);
//        String abc="123";
//
//    }
//
//    @Override
//    public void run() throws Exception {
////        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
////        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
//////        subscribeTo(ioTStream, new String[]{"current_value"}, new Function<StreamObservation, Void>() {
//////            @Override
//////            public Void apply(StreamObservation streamObservation) {
//////                return null;
//////            }
//////        });
////
////        List<StreamObservation>  observations = getObservations(1);
////        observations.get(0).getAttributes();
////        String abc="123";
////        byte[] observationModelJson = Files.readAllBytes(Paths.get("samples/Observation.json"));
////        StreamObservation observation = StreamObservation.fromJson(observationModelJson);
////        //observation.
//        //pushObservation(observation);
//
//    }
//
//
//
//
////    @Override
////    public List<String> getEntityURIs(String query) {
////        return null;
////    }
//}
