//package com.agtinternational.iotcrawler.orchestrator.apps;
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
