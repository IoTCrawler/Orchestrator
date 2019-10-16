
package com.agtinternational.iotcrawler.orchestrator;

import com.agtinternational.iotcrawler.orchestrator.clients.IotBrokerDataClient;
import com.agtinternational.iotcrawler.core.models.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class IoTBrokerDataClientTest extends EnvVariablesSetter{

    IotBrokerDataClient dataClient;
    private final static String httpServerUrl = "http://10.67.1.41:3001/notify";

    @Before
    public void init(){
        dataClient = new IotBrokerDataClient();
    }

    @Ignore
    @Test
    public void pushObservationsTest(){
        byte[] model = new byte[0];
        try {
            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamObservation streamObservation = StreamObservation.fromJson(model);
        dataClient.pushObservations(Arrays.asList(new StreamObservation[]{ streamObservation }));
        String abc = "abc";
    }

    @Ignore
    @Test
    public void getObservationsTest() throws Exception {
        byte[] model = new byte[0];
        try {
            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamObservation streamObservation = StreamObservation.fromJson(model);

        List<StreamObservation> list = dataClient.getObservations(streamObservation.getURI(),0);
        String abc = "abc";
    }



}

