import com.agtinternational.iotcrawler.fiware.clients.NGSILD;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.orange.ngsi2.model.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private Logger LOGGER = LoggerFactory.getLogger(NgsiLDClientTest.class);

    String serverUrl = "http://localhost:3000/ngsi-ld/";

    private NgsiLDClient ngsiLdClient;
    private EntityLD entity;


    @Rule
    public ExpectedException thrown = ExpectedException.none();
    //private HttpHeaders httpHeaders;

    // docker run -d -t -p 1026:1026 fiware/orion-ld

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][]{
                //new Object[]{ createEntity() },
                new Object[]{ readEntity("samples/Vehicle.json") },
                //new Object[]{ readEntity("samples/IoTStream.json") },
                //new Object[]{ readEntity("samples/TemperatureSensor.json") }
                //new Object[]{ readEntity("samples/ObservableProperty.json") }
        });
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
                entity = readEntity("samples/Vehicle.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private static EntityLD readEntity(String path) throws Exception {

        byte[] entityJson = Files.readAllBytes(Paths.get(path));
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityJson));
        return entityLD;
    }

    @Order(1)
    @Test
    public void addEntityTest() throws Exception {
        Semaphore reqFinished = new Semaphore(0);

        final Boolean[] success = {false};
        ListenableFuture<Void> req = ngsiLdClient.addEntity(entity);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        reqFinished.acquire();
        if(success[0])
            Assert.assertTrue("Entity added", true);
        else
            Assert.fail("Failed to add entity");
        LOGGER.info("Entity added");
    }

    @Order(2)
    @Test
    public void getByIdTest() throws ExecutionException, InterruptedException {
        Collection<String> ids = Arrays.asList(new String[]{ entity.getId()  });
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });  //Scorpio requires type!
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(ids, null, types, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertNotNull(entities.getItems().size()>0);
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
        String test="123";
    }

    @Order(2)
    @Test
    public void getByTypeTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });
        //Collection<String> types = Arrays.asList(new String[]{ ".*" });
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertNotNull(entities.getItems().size()>0);
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
    }

    @Order(2)
    @Test
    public void getAttributesTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });
        Collection<String> attributes = Arrays.asList(new String[]{ "brandName" });
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, attributes, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertNotNull(entities.getItems().size()>0);
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
    }



    @Order(3)
    @Test
    public void queryTest() throws ExecutionException, InterruptedException {
        Collection<String> types = Arrays.asList(new String[]{ entity.getType() });
        String query = "http://example.org/vehicle/brandName==\"Mercedes\"";
        Paginated<EntityLD> entities = ngsiLdClient.getEntities(null, null, types, null,query,null, null, 0, 0, false).get();
        Assert.assertNotNull(entities.getItems());
        Assert.assertNotNull(entities.getItems().size()>0);
        //Assert.assertTrue(entities.getItems().get(0).toJsonObject().equals(entity.toJsonObject()));
        String test="123";
    }

    @Order(3)
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
    public void updateEntityTest() throws ExecutionException, InterruptedException {
        Semaphore reqFinished = new Semaphore(0);

        ListenableFuture<Void> req = ngsiLdClient.updateEntity(entity.getId(), entity.getType(), new HashMap<>((Map)entity.getAttributes()), false);
        final Boolean[] success = {false};
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Entity updated");
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        reqFinished.acquire();

        if(success[0])
            Assert.assertTrue("Entity updated", true);
        else
            Assert.fail("Failed to update entity");

        LOGGER.info("Entity updated");
    }


    @Order(7)
    @Test
    public void deleteEntityTest() throws ExecutionException, InterruptedException {
        Semaphore reqFinished = new Semaphore(0);

        ListenableFuture<Void> req = ngsiLdClient.deleteEntity(entity.getId(), entity.getType());
        final Boolean[] success = {false};
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        reqFinished.acquire();

        if(success[0])
            Assert.assertTrue("Entity deleted", true);
        else
            Assert.fail("Failed to delete entity");

        LOGGER.info("Entity deleted");
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
