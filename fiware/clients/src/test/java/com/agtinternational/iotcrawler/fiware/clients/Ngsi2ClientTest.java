/*-
 * #%L
 * fiware-clients
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
/*
 * Copyright (C) 2016 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed till in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import com.agtinternational.iotcrawler.fiware.clients.PatchedNgsi2Client;
import com.orange.ngsi2.client.Ngsi2Client;
import com.orange.ngsi2.exception.Ngsi2Exception;
import com.orange.ngsi2.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests for Ngsi2Client
 */
public class Ngsi2ClientTest {

    //private final static String serverUrl = "http://10.67.1.41:1026/";
    String serverUrl = "http://localhost:1026";
    String accumulatorServerUrl = "http://10.67.1.41:3001/";

    private Ngsi2Client ngsi2Client;
    private Entity entity;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // docker run -d -t -p 1026:1026 fiware/orion-ld

    @Before
    public void init() throws Exception {
        //ngsi2Client = new Ngsi2Client(new CustomAsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory()), serverUrl);
        ngsi2Client = new PatchedNgsi2Client(new AsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory()), serverUrl);
        //asyncRestTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter(Utils.objectMapper)));
        entity = createEntity();
        //accomulatorServerUrl = new URL(accumulatorServerUrl);
    }

    private Entity createEntity() {
        Map<String, Attribute> attributes = new HashMap<String, Attribute>(){{
            put("location", new Attribute(){{ setType(Optional.of("City")); setValue("Madrid"); }});
            put("temperature", new Attribute(){{ setType(Optional.of("Number")); setValue(23.8); }});

        }};

        //Entity ent = new Entity("User_"+System.currentTimeMillis(), "User", attributes);
        Entity ent = new Entity("Sensor1", "Sensor", attributes);
        return ent;
    }

    private Subscription createSubscription(String subscriptionId) throws MalformedURLException {
        SubjectEntity subjectEntity = new SubjectEntity(){{ setId(Optional.of(entity.getId())); setType(Optional.of(entity.getType())); }};

        Condition pressureCondition = new Condition();
        pressureCondition.setAttributes(Arrays.asList(new String[]{ "temperature" }));

        SubjectSubscription subjectSubscription = new SubjectSubscription(Arrays.asList(new SubjectEntity[]{ subjectEntity }), pressureCondition);

        Notification notification = new Notification();
        notification.setAttributes(Arrays.asList(new String[]{ "location" }));
        notification.setCallback(new URL(accumulatorServerUrl));
        Subscription subscription = new Subscription(subscriptionId, subjectSubscription, notification, Instant.MAX,  Subscription.Status.active);
        return subscription;
    }

    @Test
    public void testGetV2_ServerError() throws Exception {
        thrown.expect(Ngsi2Exception.class);
        thrown.expectMessage("error: 500 | description: Internal Server Error | affectedItems: [item1, item2, item3]");
        ngsi2Client.getV2().get();
    }

    @Test
    public void testGetV2_ClientError() throws Exception {
        thrown.expect(Ngsi2Exception.class);
        thrown.expectMessage("error: 400 | description: Bad Request | affectedItems: []");

        ngsi2Client.getV2().get();
    }

    @Test(expected = HttpMessageNotReadableException.class)
    public void testGetV2_SyntaxError() throws Exception {

        ngsi2Client.getV2().get();
    }

    //requires unique entity, otherwise fails
    @Test
    public void addEntityTest() throws Exception {

        Semaphore reqFinished = new Semaphore(0);
        ListenableFuture<Void> req = ngsi2Client.addEntity(entity);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Entity added");
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed to add entity");
                throwable.printStackTrace();
                reqFinished.release();
            }


        });
        reqFinished.acquire();

        //ListenableFuture<Entity> entity = ngsi2Client.getEntity(ent.getId(), ent.getType(), ent.getAttributes().keySet());
        //String extractAttributes = "123";

    }

    @Test
    public void updateEntityTest() throws Exception {

        Semaphore reqFinished = new Semaphore(0);
        //String entityId, String type, Map<String, Attribute> attributes
        Entity entity = createEntity();
        Map<String, Attribute>  attributes = entity.getAttributes();
        attributes.put("temperature", new Attribute(){{ setType(Optional.of("Number")); setValue(Math.random()); }});

        ListenableFuture<Void> req = ngsi2Client.updateEntity(entity.getId(), entity.getType(), attributes,false);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Entity updated");
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed to update entity");
                throwable.printStackTrace();
                reqFinished.release();
            }


        });
        reqFinished.acquire();

        //ListenableFuture<Entity> entity = ngsi2Client.getEntity(ent.getId(), ent.getType(), ent.getAttributes().keySet());
        //String extractAttributes = "123";

    }

    //fails
    @Test
    public void addSubscriptionTest() throws Exception {

        Subscription subscription = createSubscription("subscriptionId");
        ListenableFuture<String> req = ngsi2Client.addSubscription(subscription);
        Semaphore reqFinished = new Semaphore(0);
        req.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println("Subscription "+s+" added");
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed to add subscription");
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        reqFinished.acquire();
    }

    @Test
    public void updateSubscriptionTest() throws Exception {

        String subscriptionId = "5c87bc9ba351fbf19a3d413c";
        Subscription subscription = createSubscription(subscriptionId);
        ListenableFuture<Void> req = ngsi2Client.updateSubscription(subscriptionId, subscription);
        Semaphore reqFinished = new Semaphore(0);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                System.out.println("Subscription "+subscriptionId+" updated");
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed to add subscription");
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        reqFinished.acquire();
    }


    @Test
    public void registrationTest() throws Exception {

        Registration registration = new Registration();
        SubjectRegistration subjectRegistration = new SubjectRegistration();
        subjectRegistration.setAttributes(Arrays.asList(new String[]{ "temperature", "pressure" }));

        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(Optional.of(entity.getId()));
        subjectEntity.setType(Optional.of(entity.getType()));

        subjectRegistration.setEntities(Arrays.asList(new SubjectEntity[]{ subjectEntity }));
        //subjectRegistration.setAttributes();

        registration.setSubject(subjectRegistration);
        registration.setCallback(new URL(accumulatorServerUrl));
        ListenableFuture<Void> req = ngsi2Client.addRegistration(registration);
        Semaphore reqFinished = new Semaphore(0);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Registration  added");
                reqFinished.release();
            }
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed to add registration");
                throwable.printStackTrace();
                reqFinished.release();
            }
        });
        reqFinished.acquire();
        String abc = "123";
    }



    @Test
    public void testGetV2_OK() throws Exception {

        Map<String, String> endpoints = ngsi2Client.getV2().get();
        assertEquals(4, endpoints.size());
        assertNotNull("/v2/entities", endpoints.get("entities_url"));
    }

    /*
     * Entities requests
     */

    @Test
    public void getEntitiesTest() throws Exception {
//
//        mockServer.expect(requestTo(iotBrokerUrl + "/v2/entities"))
//                .andExpect(method(HttpMethod.GET))
//                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
//                .andRespond(withSuccess(Utils.loadResource("json/getEntitiesResponse.json"), MediaType.APPLICATION_JSON));

        Paginated<Entity> entities = ngsi2Client.getEntities(null, null, null, null, 0, 0, false).get();
        String test="123";
//        assertEquals(3, entities.getItems().size());
//        assertNotNull(entities.getItems().get(0));
//        assertNotNull(entities.getItems().get(1));
//        assertNotNull(entities.getItems().get(2));
//        assertEquals("DC_S1-D41", entities.getItems().get(0).getId());
//        assertEquals("Room", entities.getItems().get(0).getType());
//        assertEquals(35.6, entities.getItems().get(0).getAttributes().get("temperature").getObject());
//        assertEquals(0, entities.getOffset());
//        assertEquals(0, entities.getLimit());
//        assertEquals(0, entities.getTotal());
    }


}
