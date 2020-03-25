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

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.fiware.models.subscription.Endpoint;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.SubscriptionLD;
import com.orange.ngsi2.model.Condition;
import com.orange.ngsi2.model.SubjectEntity;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import com.agtinternational.iotcrawler.core.models.*;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_ORCHESTRATOR_URL;

public class ExampleIoTCrawlerAppTest {

    Logger LOGGER = LoggerFactory.getLogger(ExampleIoTCrawlerAppTest.class);
    ExampleIoTCrawlerExampleApp app;

    @Before
    public void init() throws Exception {
        EnvVariablesSetter.init();
        app = new ExampleIoTCrawlerExampleApp(System.getenv(IOTCRAWLER_ORCHESTRATOR_URL));
        app.init();
    }


    @Ignore
    @Test
    public void runTest() throws Exception {
        //app.getS
        app.run();
        String abc="213";
    }



    @Ignore
    @Test
    public void getAllStreamsTest() throws Exception {
        //app.getS
        List<IoTStream> streams = app.getStreams(null, null,0,0);
        System.out.println(streams.size()+" streams returned");
        for(IoTStream stream : streams)
            System.out.println(stream.toJsonLDString());
        String abc="213";
    }

    @Ignore
    @Test
    public void getSensorsTest() throws Exception {
        //app.getS
        List<Sensor> sensors = app.getSensors(null, 0,0);
        System.out.println(sensors.size()+" sensors returned");
        for(Sensor sensor : sensors)
            System.out.println(sensor.toJsonLDString());
        String abc="213";
    }

//    @Ignore
//    @Test
//    public void getStreamByIdTest() throws Exception {
//
//        FilteringSelector selector=new FilteringSelector.Builder()
//                //.subject("http://purl.org/iot/ontology/iot-stream#gateway_00055110D732_device_8_sensor_64_stream")
//                //.subject("iotc:gateway_00055110D732_device_8_sensor_64_stream")
//                .subject("urn:ngsi-ld:Vehicle:A188")
//                .build();
//
//        List<IoTStream> streams = app.getStreams(selector, 0);
//        String abc="213";
//    }
//
//    @Ignore
//    @Test
//    public void getStreamByPropertyValueTest() throws Exception {
//        FilteringSelector selector=new FilteringSelector.Builder()
//                .predicate("http://purl.org/iot/ontology/iot-stream#madeBySensor")
//                .object("http://www.w3.org/ns/sosa/gateway_00055110D732_device_8_sensor_64")
//                .build();
//
//        List<IoTStream> streams = app.getStreams(selector, 0);
//        String abc="213";
//    }


    @Ignore
    @Test
    public void subscribeTest() throws Exception {
        LOGGER.info("subscribeTest()");
        byte[] iotStreamModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamModelJson);
        //IoTStream iotObservationModel = IoTStream.fromJson(iotStreamModel);

        //String entityUri = "http://purl.org/iot/ontology/iot-stream#StreamObservation_gateway_00055110D732_device_8_sensor_64_stream";
        String entityUri = ioTStream.getURI();

        Semaphore reqFinished = new Semaphore(0);

        SubjectEntity subjectEntity = new SubjectEntity(){{ setId(Optional.of(ioTStream.getURI())); setType(Optional.of(ioTStream.getTypeURI())); }};

        Condition pressureCondition = new Condition();
        pressureCondition.setAttributes(Arrays.asList(new String[]{ "temperature" }));

        //SubjectSubscription subjectSubscription = new SubjectSubscription(Arrays.asList(new SubjectEntity[]{ subjectEntity }), pressureCondition);


        NotificationParams notification = new NotificationParams();
        notification.setAttributes(Arrays.asList(new String[]{ "location" }));
        notification.setEndpoint(new Endpoint(new URL(System.getenv(IOTCRAWLER_ORCHESTRATOR_URL).replace("/ngsi-ld","/notify")), ContentType.APPLICATION_JSON));
        SubscriptionLD subscription = new SubscriptionLD(
                UUID.randomUUID().toString(),
                Arrays.asList(new SubjectEntity[]{ subjectEntity }),
                notification,
                null,
                null);
        String id = app.subscribeTo(subscription, new Function<StreamObservation, Void>() {
            @Override
            public Void apply(StreamObservation streamObservation) {
                Map<String, List<Object>> properties = streamObservation.getProperties();
                return null;
            }
        });
        Assert.notNull(id);
        LOGGER.info("Subscription succeeded {}", id);
    }

//    @Ignore
//    @Test
//    public void pushObservationsTest() throws Exception {
//        byte[] model = new byte[0];
//        try {
//            model = Files.readAllBytes(Paths.get("samples/Observation.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        StreamObservation streamObservation = StreamObservation.fromJson(model);
//
//        app.pushObservationsToBroker(Arrays.asList(new StreamObservation[]{streamObservation}));
//
//        String abc = "abc";
//    }

    @Ignore
    @Test
    public void getObservationsTest() throws Exception {
        List<StreamObservation> list = app.getObservations("StreamID",0,0);
        list.get(0).getModel();
        String abc = "abc";
    }
}
