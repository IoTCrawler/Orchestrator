/*-
 * #%L
 * fiware-clients
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
package com.agtinternational.iotcrawler.fiware.clients;
import eu.neclab.iotplatform.iotbroker.client.Southbound;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscoveryServicesTest {

    //private final static String serverUrl = "http://155.54.95.237:8065";
    private final static String serverUrl = "http://localhost:8065";

    Southbound iotBrokerClient;

    @Before
    public void init(){
        //docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled"
        iotBrokerClient = new Southbound();
        iotBrokerClient.setNgsi9url(serverUrl);
        iotBrokerClient.setNgsi9RemoteUrl(serverUrl);
        iotBrokerClient.setNgsi9rootPath("ngsi9");
        iotBrokerClient.setDefaultContentType("application/json");
    }

    private ContextElement initNoiseSensor(){
        List<ContextMetadata> metadata = new ArrayList<ContextMetadata>(){{ add(new ContextMetadata("units", URI.create("units"), "dB")); }};
        ContextAttribute noiseLevel = new ContextAttribute("noiseLevel", URI.create("float"), "66", metadata);

        ContextElement ret = new ContextElement();
        ret.setEntityId(new EntityId("noiseSensor", URI.create("NoiseSensor"), false));
        ret.setContextAttributeList(new ArrayList<ContextAttribute>(){{ add(noiseLevel); }});
        ret.setDomainMetadata(new ArrayList<ContextMetadata>());

        return ret;
    }

    @Test
    public void registerContextTest() throws URISyntaxException {

        ContextElement sensor = initNoiseSensor();
        ContextRegistration contextRegistration = new ContextRegistration();
        contextRegistration.setListEntityId(Arrays.asList(sensor.getEntityId()));
        contextRegistration.setProvidingApplication(new URI("http://provider1"));

        List<ContextRegistration> contextRegistrationList = new ArrayList<>();
        contextRegistrationList.add(contextRegistration);

        RegisterContextRequest request = new RegisterContextRequest();
        request.setContextRegistrationList(contextRegistrationList);
        //request.setDuration();

        RegisterContextResponse response = iotBrokerClient.registerContext(request);

        if(response.getErrorCode()!=null && response.getErrorCode().getCode()!=200)
            Assert.fail(response.getErrorCode().toJsonString());

        String registrationId = response.getRegistrationId();

        String extractAttributes="123";
    }


    @Test
    public void discoverContextAvailabilityTest() {

        ContextElement sensor = initNoiseSensor();
        DiscoverContextAvailabilityRequest request = new DiscoverContextAvailabilityRequest();
        request.setEntityIdList(Arrays.asList(sensor.getEntityId()));
        DiscoverContextAvailabilityResponse response = iotBrokerClient.discoverContextAvailability(request);
        if(response.getErrorCode()!=null && response.getErrorCode().getCode()!=200)
            Assert.fail(response.getErrorCode().toJsonString());

        List<ContextRegistrationResponse> list = response.getContextRegistrationResponse();
        String extractAttributes="123";
    }

    @Test
    public void subscribeContextAvailabilityTest() throws DatatypeConfigurationException {

        ContextElement sensor = initNoiseSensor();

        SubscribeContextAvailabilityRequest request = new SubscribeContextAvailabilityRequest();
        request.setReference(serverUrl);
        request.setEntityIdList(Arrays.asList(sensor.getEntityId()));
        request.setAttributeList(Arrays.asList(new String[]{ "noiseLevel" }));
        //request.setRestriction(new Restriction());

        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(new String[]{ "noiseLevel" }), null);

        //request.setSubscriptionId();
        javax.xml.datatype.Duration duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
        request.setDuration(duration);
        SubscribeContextAvailabilityResponse response = iotBrokerClient.subscribeContextAvailability(request);
        if(response.getErrorCode()!=null)
            Assert.fail(response.getErrorCode().toJsonString());

        String extractAttributes="123";
    }

//    @Test
//    public void subscribeContextTest() throws DatatypeConfigurationException {
//
//        ContextElement sensor = initNoiseSensor();
//
//        SubscribeContextRequest request = new SubscribeContextRequest();
//        request.setReference(serverUrl);
//        request.setEntityIdList(Arrays.asList(sensor.getEntityId()));
//        request.setAttributeList(Arrays.asList(new String[]{ "noiseLevel" }));
//        //request.setRestriction(new Restriction());
//
//        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(new String[]{ "noiseLevel" }), null);
//
//        //request.setSubscriptionId();
//        javax.xml.datatype.Duration duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
//        request.setDuration(duration);
//        SubscribeContextResponse response = iotBrokerClient.subscribeContext(request, URI.create(serverUrl+"/"+iotBrokerClient.getNgsi9rootPath()));
//        //SubscribeContextResponse response = iotBrokerClient.subscribeContext(request, URI.create(serverUrl));
//        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null)
//            Assert.fail(response.getSubscribeError().getStatusCode().toJsonObject());
//
//        String extractAttributes="123";
//    }

//    @Test
//    public void subscriptionTest() throws IOException {
//
//        HttpTestServer httpTestServer = new HttpTestServer();
//        httpTestServer.initHttpServer();
//
//
//    }

    }
