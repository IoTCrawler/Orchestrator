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
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;

import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.agtinternational.iotcrawler.fiware.models.subscription.Endpoint;
import com.agtinternational.iotcrawler.fiware.models.subscription.EntityInfo;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.orange.ngsi2.model.*;
import org.apache.http.entity.ContentType;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



/**
 * Tests for Ngsi2Client
 */
@RunWith(Parameterized.class)
public class NgsiLDClientTest extends EnvVariablesSetter{

    private static Logger LOGGER = LoggerFactory.getLogger(NgsiLDClientTest.class);

    String serverUrl;

    private NgsiLDClient ngsiLdClient;
    private EntityLD entity;


    @Rule
    public ExpectedException thrown = ExpectedException.none();
    //private HttpHeaders httpHeaders;

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {

        List<Object[]> objects = new ArrayList<>();
        File folder = new File("samples");
        if(folder.exists()) {
            //try {
                Files.list(folder.toPath()).forEach(file->{
                    try {
                        EntityLD entity = readEntity(file);
                        objects.add(new Object[]{ entity });
                    } catch (Exception e) {
                        LOGGER.error("Faield to parse {}", file.toString());
                        e.printStackTrace();
                    }

                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return objects;
//        return Arrays.asList(new Object[][]{
//                //new Object[]{ createEntity() },
//                //new Object[]{ readEntity("samples/Vehicle.json") },
//                new Object[]{ readEntity("samples/IoTStream.json") },
//                //new Object[]{ readEntity("samples/TemperatureSensor.json") }
//                //new Object[]{ readEntity("samples/ObservableProperty.json") }
//        });
    }

    public NgsiLDClientTest(EntityLD entityLD){
        this.entity = entityLD;

    }

    @Before
    public void init(){
        super.init();
        
        if (System.getenv().containsKey(NGSILD_BROKER_URL))
            serverUrl = System.getenv(NGSILD_BROKER_URL);

        ngsiLdClient = new NgsiLDClient(serverUrl);
        //ngsiLdClient = new NgsiLDClient(new CustomAsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory()), new NgsiLDRestTemplate(), serverUrl);
        //ngsiLdClient.asyncRestTemplate = new CustomAsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory());
        //httpHeaders = new HttpHeaders();
        //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //asyncRestTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter(Utils.objectMapper)));

        //entity = createEntity();
        if(entity==null) {
            try {
                entity = readEntity(Paths.get("samples/Vehicle.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LOGGER.info(entity.getId());
        //accomulatorServerUrl = new URL(accumulatorServerUrl);
    }



    private static EntityLD createEntity() {
        Map<String, Attribute> attributes = new HashMap<String, Attribute>(){{
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

        try {
            Paginated<EntityLD> entities = ngsiLdClient.getEntities(Arrays.asList(new String[]{entity.getId()}), null, null, null, 0, 0, false).get();
            if (!entities.getItems().isEmpty())
                deleteEntityTest();
        }
        catch (Exception e){
            if(e.getCause() instanceof HttpClientErrorException.NotFound)
                LOGGER.info("No entity existed before found");
            else e.printStackTrace();
        }

        Boolean success = ngsiLdClient.addEntitySync(entity);
        Assert.assertTrue(success);
        LOGGER.info("Entity is expected to be added. Trying to get it");

        getEntityByIdTest();

    }


    @Order(2)
    @Test
    public void getEntitiesByTypeTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{
                entity.getType()
                //"http://www.w3.org/ns/sosa/Sensor"
        });
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertTrue(entities.getItems().size()>0);
        entities.getItems().stream().forEach(e->{
            try {
                LOGGER.info(Utils.prettyPrint(e.toJsonObject()));
            }
            catch (Exception e2){
                LOGGER.warn("trouble with outputting result {}: {}", e.getId(), e2.getLocalizedMessage());
            }
        });
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
    }


    @Order(3)
    @Test
    public void getEntityByIdTest() throws ExecutionException, InterruptedException {
        Collection<String> ids = Arrays.asList(new String[]{
                //"urn:ngsi-ld:MultiSensor_AEON_Labs_ZW100_MultiSensor_6"
                entity.getId()
        });

//        Collection<String> types = Arrays.asList(new String[]{
//                entity.getType()
//                //"sosa:Sensor"
//        });  //Scorpio requires type!
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(ids, null, null, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertTrue(entities.getItems().size()>0);
        entities.getItems().stream().forEach(e->{
            try {
                LOGGER.info(Utils.prettyPrint(e.toJsonObject()));
            }
            catch (Exception e2){
                LOGGER.warn("trouble with outputting result {}: {}", e.getId(), e2.getLocalizedMessage());
            }
        });
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
        String test="123";
    }


    @Order(3)
    @Test
    public void getAttributesTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });
        Collection<String> attributes = Arrays.asList(new String[]{ entity.getAttributes().keySet().iterator().next() });
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, attributes, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertTrue(entities.getItems().size()>0);
        entities.getItems().stream().forEach(e->{
            try {
                LOGGER.info(Utils.prettyPrint(e.toJsonObject()));
            }
            catch (Exception e2){
                LOGGER.warn("trouble with outputting result {}: {}", e.getId(), e2.getLocalizedMessage());
            }
        });
    }

    @Order(4)
    @Test
    public void getByAttributeValueTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });
        String attName = entity.getAttributes().keySet().iterator().next();
        String value = entity.getAttribute(attName).getValue().toString();
        String query = attName+"==\""+value+"\"";
        //String query = "q=brandName==\"Mercedes\"";  //Scorpio
        //String query = "brandName.value=Mercedes";   //djane
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, null,query,null, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertTrue(entities.getItems().size()>0);
        entities.getItems().stream().forEach(e->{
            try {
                LOGGER.info(Utils.prettyPrint(e.toJsonObject()));
            }
            catch (Exception e2){
                LOGGER.warn("trouble with outputting result {}: {}", e.getId(), e2.getLocalizedMessage());
            }
        });
        String test="123";
    }

    @Order(5)
    @Test
    public void geoQueryTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ "http://example.org/pleyades/WeatherbitSensor" });
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(-1.173032, 38.024519));
        //GeoQuery geoQuery = new GeoQuery(GeoQuery.Relation.near, GeoQuery.Geometry.point, coordinates);
        GeoQuery geoQuery = new GeoQuery(GeoQuery.Modifier.maxDistance, 360, GeoQuery.Geometry.point, coordinates);
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, null, null, geoQuery, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertNotNull(entities.getItems().size()>0);
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
        String test="123";
    }

    @Order(6)
    @Test
    public void updateEntityTest() throws Exception {

        Map<String, Attribute> attributes = entity.getAttributes();
        entity.addAttribute(attributes.keySet().iterator().next().toString(), (Attribute)attributes.values().iterator().next());

        boolean success = ngsiLdClient.updateEntitySync(entity , false);
        if(success)
            Assert.assertTrue("Entity updated", true);
        else
            Assert.fail("Failed to update entity");

        LOGGER.info("Entity updated");
    }


    @Order(7)
    @Test
    public void deleteEntityTest() throws Exception {
        boolean success = ngsiLdClient.deleteEntitySync(entity.getId());
        if(success)
            Assert.assertTrue("Entity deleted", true);
        else
            Assert.fail("Failed to delete entity");

        LOGGER.info("Entity deleted");
    }


    //@Ignore
    @Test
    public void addSubscriptionTest() throws Exception {


        EntityInfo entityInfo = new EntityInfo(entity.getId(), entity.getType());

        NotificationParams notification = new NotificationParams();
        notification.setAttributes(Arrays.asList(new String[]{ "location" }));
        notification.setEndpoint(new Endpoint(new URL(HttpTestServer.getRefenceURL()), ContentType.APPLICATION_JSON));

        Subscription subscription = new Subscription(
                UUID.randomUUID().toString(),
                Arrays.asList(new EntityInfo[]{ entityInfo }),
                Arrays.asList(new String[]{ "temperature" }),
                notification,
                null,
                null);


        String subscriptionId = ngsiLdClient.addSubscriptionSync(subscription);
        Assert.assertNotNull(subscriptionId);
        LOGGER.info("Subscription created");
    }

    @Ignore
    @Test
    public void addRegistrationTest() throws Exception {
        Semaphore reqFinished = new Semaphore(0);

        SubjectRegistration subjectRegistration = new SubjectRegistration();
        subjectRegistration.setEntities(Arrays.asList(new SubjectEntity[]{ new SubjectEntity() }));

        Registration registration = new Registration();
        registration.setId("urn:ngsi-ld:ContextSourceRegistration:csr1a3456");
        registration.setSubject(subjectRegistration);
        //registration.set


        ListenableFuture<Void> req = ngsiLdClient.addRegistration(registration);
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

        LOGGER.info("Registration added");
    }
}
