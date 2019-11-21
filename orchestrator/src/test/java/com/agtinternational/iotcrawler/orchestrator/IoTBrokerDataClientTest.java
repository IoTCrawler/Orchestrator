
package com.agtinternational.iotcrawler.orchestrator;

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

