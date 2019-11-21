//package com.agtinternational.iotcrawler.orchestrator.apps;

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
