package com.agtinternational.iotcrawler.fiware.clients;

/*-
 * #%L
 * fiware-clients
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

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.agtinternational.iotcrawler.fiware.models.subscription.Endpoint;
import com.agtinternational.iotcrawler.fiware.models.subscription.EntityInfo;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.orange.ngsi2.model.Attribute;
import com.orange.ngsi2.model.Paginated;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.annotation.Order;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class NgsiLDClientTests {

    private NgsiLDClient ngsiLdClient;
    String serverUrl;
    private EntityLD entity;

    @Before
    public void init(){
        EnvVariablesSetter.init();

        if (System.getenv().containsKey(NGSILD_BROKER_URL))
            serverUrl = System.getenv(NGSILD_BROKER_URL);

        ngsiLdClient = new NgsiLDClient(serverUrl);

        //entity = createEntity();
        if(entity==null) {
            try {
                entity = readEntity(Paths.get("samples/Vehicle.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static EntityLD createEntity() {
        Map<String, Object> attributes = new HashMap<String, Object>(){{
            put("brandName", new Property(){{ setType(Optional.of(NGSILD.Property)); setValue("Mercedes"); }});
            put("isParked", new Relationship("ngsi-ld:OffStreetParking:Downtown1"){{
                        setType(Optional.of(NGSILD.Relationship));
                        //setObject("ngsi-ld:OffStreetParking:Downtown1");

                        Map<String, Object> attributes2 = new HashMap<>();
                        //attributes2.put("object", "ngsi-ld:OffStreetParking:Downtown1");
                        attributes2.put("observedAt", "2017-07-29T12:00:04");
                        attributes2.put("providedBy", new Relationship("urn:ngsi-ld:Person:Bob"));
                        setAttributes(attributes2);
                    }}
            );
            //put("http://purl.org/iot/ontology/iot-stream#temperature", new Attribute(){{ setType(Optional.of("Number")); setObject(23.8); }});

        }};

        //Entity ent = new Entity("User_"+System.currentTimeMillis(), "User", attributes);
        EntityLD ent = new EntityLD("urn:ngsi-ld:Vehicle:A4571", "urn:ngsi-ld:Vehicle", attributes);
        return ent;
    }

    private static EntityLD readEntity(Path path) throws Exception {

        byte[] entityJson = Files.readAllBytes(path);
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityJson));
        return entityLD;
    }

    @Order(1)
    @Test
    public void addEntityTest() throws Exception {

//        try {
//            Paginated<EntityLD> entities = ngsiLdClient.getEntities(Arrays.asList(new String[]{entity.getId()}), null, null, null, 0, 0, false).get();
//            if (!entities.getItems().isEmpty())
//                deleteEntityTest();
//        }
//        catch (Exception e){
//            if(e.getCause() instanceof HttpClientErrorException.NotFound)
//                LOGGER.info("No entity existed before found");
//            else e.printStackTrace();
//        }

        Boolean success = ngsiLdClient.addEntitySync(entity);
        Assert.assertTrue(success);
        getEntityByIdTest();

    }

    @Order(3)
    @Test
    public void getEntityByIdTest() throws Exception {
        EntityLD entityLD = ngsiLdClient.getEntitySync("urn:ngsi-ld:HouseholdStateObservation_Household1",null,null);
        String abc = "asd";
    }


    @Order(3)
    @Test
    public void getEntitiesByType() throws Exception {
        List<String> types = Arrays.asList(new String[]{"http://www.w3.org/ns/sosa/Platform"});
        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(null,null,types,null,null,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }




    @Order(3)
    @Test
    public void getEntitiesByIdTest() throws Exception {
        List<String> ids = Arrays.asList(new String[]{
                //"urn:ngsi-ld:Stream:ora10"
                "urn:ngsi-ld:HouseholdStateObservation_Household1"
        });
        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(ids,null,null,null,null,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }



    @Order(3)
    @Test
    public void getEntitiesByParameterTest() throws Exception {

        Map query = new HashMap<String, Pair>();
        //query.put("http://www.w3.org/2000/01/rdf-schema#label", "\"homee_00055110D732\"");
        //query.put("http://www.agtinternational.com/ontologies/ngsi-ld#alternativeType","\"http://agtinternational/smartHomeApp#HouseholdStateStream\"");
        query.put("http://dummyurl/speed", Pair.of(">", "\"55\""));
        query.put("http://dummyurl/speed2", 55);

        Collection<String> types = Arrays.asList(new String[]{
                "http://purl.org/iot/ontology/iot-stream#IotStream"
                //"http://www.w3.org/ns/sosa/Platform"
        });

        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(null,null,types,null,query,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }

    @Order(3)
    @Test
    public void getSubsciptionsTest() throws Exception {
        Paginated<Subscription> ret = ngsiLdClient.getSubscriptionsSync(0,5 ,true);
        List<Subscription> entities = ret.getItems();
        String abc = "asd";
    }

    @Order(3)
    @Test
    public void deleteSubsciptions() throws Exception {
        Paginated<Subscription> req = ngsiLdClient.getSubscriptionsSync(0,0,false);
        for(Subscription subscription: req.getItems()) {
            ngsiLdClient.deleteSubscriptionSync(subscription.getId());
        }
    }


    @Order(6)
    @Test
    public void updateEntityTest() throws Exception {

        Map<String, Object> attributes = entity.getAttributes();
        String name = attributes.keySet().iterator().next().toString();
        Object value = ((Attribute)attributes.values().iterator().next()).getValue()+"_"+System.currentTimeMillis();
        entity.addAttribute(name, value);

        boolean success = ngsiLdClient.updateEntitySync(entity , false);
        if(success)
            Assert.assertTrue("Entity updated", true);
        else
            Assert.fail("Failed to update entity");

    }

    @Order(6)
    @Test
    @Ignore
    public void updateEntityTest2() throws Exception {

        EntityLD entity2 = ngsiLdClient.getEntitySync("urn:ngsi-ld:Sensor_OpenHAB_zwave:device:e010f31b:node2_zwave:device:e010f31b:node2:sensor_power", "http://www.w3.org/ns/sosa/Sensor", Collections.emptyList());
        Map<String, Object> attributes = entity2.getAttributes();

//        String name = attributes.keySet().iterator().next().toString();
//        Object value = ((Attribute)attributes.values().iterator().next()).getValue()+"_"+System.currentTimeMillis();
        entity2.addAttribute("https://faultdetection.org/fd#enable", new Property(1));

        boolean success = ngsiLdClient.updateEntitySync(entity2 , false);
        if(success)
            Assert.assertTrue("Entity updated", true);
        else
            Assert.fail("Failed to update entity");

    }

    @Order(3)
    @Test
    public void updateAttributeTest() throws Exception {
        Map<String, Object> attributes = entity.getAttributes();

        String name = attributes.keySet().iterator().next().toString();
        Attribute attribute = ((Attribute)attributes.values().iterator().next());
        attribute.setValue(attribute.getValue().toString()+"_"+System.currentTimeMillis());

        ngsiLdClient.updateAttributeSync(entity.getId(), entity.getType(), name, attribute);
        String abc = "asd";
    }

    @Order(3)
    @Test
    public void deleteAttributeTest() throws Exception {

        ngsiLdClient.deleteAttributeSync(entity.getId(),entity.getType(), "brandName");
        String abc = "asd";
    }


    @Test
    public void addSubscriptionTest() throws Exception {


        EntityInfo entityInfo = new EntityInfo(entity.getId(), entity.getType());

        NotificationParams notification = new NotificationParams();
        notification.setAttributes(Arrays.asList(new String[]{ "location" }));
        notification.setEndpoint(new Endpoint(new URL(HttpTestServer.getRefenceURL()), ContentType.APPLICATION_JSON));
        String subscriptionId = "urn:"+UUID.randomUUID().toString();
        Subscription subscription = new Subscription(
                subscriptionId,
                Arrays.asList(new EntityInfo[]{ entityInfo }),
                Arrays.asList(new String[]{ "temperature" }),
                notification,
                null,
                null);


        ngsiLdClient.addSubscriptionSync(subscription);
        Assert.assertNotNull(subscriptionId);

    }

}
