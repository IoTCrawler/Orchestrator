package com.agtinternational.iotcrawler;

/*-
 * #%L
 * example-app
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.core.clients.IoTCrawlerRESTClient;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import com.agtinternational.iotcrawler.core.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class ExampleIoTCrawlerExampleApp extends IoTCrawlerRESTClient {

    public static final String agtSmartHomeNS = "http://www.agtinternational.com/ontologies/SmartHome#";
    Semaphore finishedMutex = new Semaphore(0);
    private Logger LOGGER = LoggerFactory.getLogger(ExampleIoTCrawlerExampleApp.class);

    public ExampleIoTCrawlerExampleApp(String orchestratorURL) {
        super(orchestratorURL);
    }

    @Override
    public void init(){
        super.init();


    }

    @Override
    public void run() throws Exception {
        super.run();
        List<IoTStream> streams = getStreams(null, null,0,0);
//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (IoTStream ioTStream : streams){
//
//                    String[] attributes = new String[]{ agtSmartHomeNS + "current_value", agtSmartHomeNS + "state", };
//                    //String[] attributes = new String[]{ ".*"};
//                    NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
//
//                    try {
//                        subscribeTo(ioTStream.getURI(), attributes, Arrays.asList(new NotifyCondition[]{notifyCondition}), new Restriction(), getNotifyFunction());
//                    }
//                    catch (Exception e){
//                        LOGGER.error("Failed to subscribe to stream {}", ioTStream.getURI());
//                        e.printStackTrace();
//                    }
//                }
//
//            };
//        });
//        t2.start();
        finishedMutex.acquire();
        String abc = "123";
//        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
//        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
////        subscribeTo(ioTStream, new String[]{"current_value"}, new Function<StreamObservation, Void>() {
////            @Override
////            public Void apply(StreamObservation streamObservation) {
////                return null;
////            }
////        });
//
//        //List<StreamObservation>  observations = getObservations(1);
//        byte[] observationModelJson = Files.readAllBytes(Paths.get("samples/Observation.json"));
//        StreamObservation observation = StreamObservation.fromJson(observationModelJson);
//        //observation.
//        //pushObservation(observation);

    }


    public Function<StreamObservation, Void> getNotifyFunction(){
        return new Function<StreamObservation, Void>() {
            @Override
            public Void apply(StreamObservation streamObservation) {
                System.out.println("Got it!");
//                RDFNode currentValue = streamObservation.getAttribute(agtSmartHomeNS + "current_value");
//                RDFNode state = streamObservation.getAttribute(agtSmartHomeNS + "state");
//
//                Double currentValueDouble = 0.0;
//                if(streamObservation.getAttribute(agtSmartHomeNS + "current_value")!=null)
//                    currentValueDouble = streamObservation.getAttribute(agtSmartHomeNS + "current_value").asLiteral().getDouble();
//
//                if(streamObservation.getAttribute(agtSmartHomeNS + "state")!=null)
//                    currentValueDouble= streamObservation.getAttribute(agtSmartHomeNS + "state").asLiteral().getDouble();
//                LOGGER.info("Current consumption: {}", currentValue);
                //getcurrentUsage().put(streamObservation.getURI(), currentValueDouble);
                //List<Pair<String, Object>> attributes = streamObservation.getAttributes();
                return null;
            }
        };
    }






}
