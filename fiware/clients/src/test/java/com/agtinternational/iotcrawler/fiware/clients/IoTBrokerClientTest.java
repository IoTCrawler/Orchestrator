import com.agtinternational.iotcrawler.fiware.clients.CustomSubscribeContextRequest;
import com.agtinternational.iotcrawler.fiware.clients.PatchedSouthBound;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IoTBrokerClientTest {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    //private final static String serverUrl = "http://localhost:8060/ngsi9";
    //private final static String serverUrl = "http://localhost:8065";
    private final static String serverUrl = "http://localhost:8060/ngsi10";
    private final static String httpServerUrl = "http://10.67.1.41:3001/notify";

    PatchedSouthBound iotBrokerClient;
    ContextElement sensor;

    @Before
    public void init() throws IOException {
        //Requires enabling of historicalAgent (database)
        //docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled"
        iotBrokerClient = new PatchedSouthBound();

        iotBrokerClient.setNgsi9url(serverUrl+"/");
        iotBrokerClient.setNgsi9RemoteUrl(serverUrl+"/");
        iotBrokerClient.setNgsi9rootPath("ngsi9");
        iotBrokerClient.setNgsi10Reference(httpServerUrl);
        iotBrokerClient.setDefaultContentType("application/json");

        sensor = initSensor();
    }

    private ContextElement initSensor() throws IOException {
        //byte[] model = Files.readAllBytes(Paths.get("samples/Observation.json"));
        byte[] model = Files.readAllBytes(Paths.get("samples/Observation2.json"));

        Object contextElement = ContextElement.parseStringToJson(new String(model), ContextElement.class);
        ContextElement ret = ((ContextElement)contextElement);
//        List<ContextMetadata> dbUnits = new ArrayList<ContextMetadata>(){{ add(new ContextMetadata("units", URI.create("units"), "dB")); }};
//        ContextAttribute noiseLevel = new ContextAttribute("noiseLevel", URI.create("float"), String.valueOf(System.nanoTime()), dbUnits);
//        List<ContextMetadata> voltUnits = new ArrayList<ContextMetadata>(){{ add(new ContextMetadata("units", URI.create("units"), "volt")); }};
//        ContextAttribute batteryLevel = new ContextAttribute("batteryLevel", URI.create("float"), String.valueOf(System.nanoTime()), voltUnits);
//        ContextAttribute status = new ContextAttribute("status", URI.create("boolean"), String.valueOf(System.nanoTime()), null);
//
//        ContextElement ret = new ContextElement();
//        ret.setEntityId(new EntityId("noiseSensor", URI.create("NoiseSensor"), false));
//        ret.setContextAttributeList(new ArrayList<ContextAttribute>(){{ add(noiseLevel); add(batteryLevel); }});
//        ret.setDomainMetadata(new ArrayList<ContextMetadata>());
//
        return ret;
    }

    private ContextElement initLightSensor(){
        List<ContextMetadata> metadata = new ArrayList<ContextMetadata>(){{ add(new ContextMetadata("units", URI.create("units"), "lux")); }};
        ContextAttribute lightLevel = new ContextAttribute("noiseLevel", URI.create("float"), "5678", metadata);

        ContextElement ret = new ContextElement();
        ret.setEntityId(new EntityId("lightSensor", URI.create("LightSensor"), false));
        ret.setContextAttributeList(new ArrayList<ContextAttribute>(){{ add(lightLevel); }});
        ret.setDomainMetadata(new ArrayList<ContextMetadata>());

        return ret;
    }

    @Test
    @Order(1)
    public void updateContextElementTest() throws Exception {
        LOGGER.info("updateContextElementTest");
        //ContextElement sensor = initSensor();

        //ContextElement sensor = initLightSensor();
        List<ContextElement> contextElements = new ArrayList<ContextElement>(){{  add(sensor); }};

        UpdateContextRequest request = new UpdateContextRequest();
        request.setContextElement(contextElements);
        request.setUpdateAction(UpdateActionType.APPEND);
        UpdateContextResponse response = iotBrokerClient.updateContext(request, URI.create(serverUrl));

        if(response.getErrorCode().getCode()!=200)
            Assert.fail(response.getErrorCode().toJsonString());

        Assert.assertEquals(response.getErrorCode().getCode(), 200);
        //queryContextElement(sensor.getEntityId());
    }


    @Test
    @Order(2)
    public void queryContextElementTest(){
        LOGGER.info("queryContextElementTest");
        QueryContextResponse response = queryContextElement(sensor.getEntityId());
        if(response.getErrorCode()!=null)
            Assert.fail(response.getErrorCode().toJsonString());

        Assert.assertNotNull(response.getListContextElementResponse());
        Assert.assertFalse(response.getListContextElementResponse().isEmpty());
        assertEquals(sensor, response.getListContextElementResponse().get(0).getContextElement());
    }

    private QueryContextResponse queryContextElement(EntityId entityId){

        QueryContextRequest request = new QueryContextRequest();
        request.setEntityIdList(new ArrayList<EntityId>(){{ add(entityId); }});

        QueryContextResponse response = iotBrokerClient.queryContext(request, URI.create(serverUrl));
        return response;
    }


    @Test
    @Order(3)
    public void queryAllContextElementsTest(){
        LOGGER.info("queryAllContextElementsTest");
        QueryContextRequest request = new QueryContextRequest();
        request.setEntityIdList(Arrays.asList(new EntityId[]{ new EntityId(){{  setId(".*"); setIsPattern(true); }} }));

        QueryContextResponse response = iotBrokerClient.queryContext(request, URI.create(serverUrl));
        List<ContextElementResponse> list = response.getListContextElementResponse();

        assertNotNull(list);
        
    }

    //works, but no effect
    @Test
    @Order(4)
    public void deleteContextElementTest() throws Exception {
        LOGGER.info("deleteContextElementTest");
        //ContextElement sensor = initLightSensor();
        ContextElement sensor = initSensor();

        List<ContextElement> contextElements = new ArrayList<ContextElement>(){{
            //add(sensor);
            add(new ContextElement(){{
                setEntityId(new EntityId(){{
                    setId("http://purl.org/iot/ontology/iot-stream/Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%291");
                    setIsPattern(false); }});
            }});
        }};


        UpdateContextRequest request = new UpdateContextRequest();
        request.setContextElement(contextElements);
        request.setUpdateAction(UpdateActionType.DELETE);
        UpdateContextResponse response = iotBrokerClient.updateContext(request, URI.create(serverUrl));
        if(response.getErrorCode().getCode()!=200)
            Assert.fail(response.getErrorCode().toJsonString());

        queryContextElement(sensor.getEntityId());
    }

    @Ignore
    @Test
    public void subscribeContextTest() throws DatatypeConfigurationException {
        LOGGER.info("subscribeContextTest");
        //ContextElement sensor = initSensor();

        CustomSubscribeContextRequest request = new CustomSubscribeContextRequest();
        request.setReference(httpServerUrl);
        request.setEntityIdList(Arrays.asList(sensor.getEntityId()));
        //request.setEntityIdList(Arrays.asList(new EntityId[]{ new EntityId(){{  setId(".*"); setIsPattern(true); }} }));
        request.setAttributeList(Arrays.asList(new String[]{ "noiseLevel" }));

        Restriction restriction = new Restriction();
        //restriction.setAttributeExpression();
        request.setRestriction(restriction);

        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(new String[]{ "noiseLevel" }), null);
        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONTIMEINTERVAL, Arrays.asList(new String[]{ "noiseLevel" }), null);

        javax.xml.datatype.Duration duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
        request.setDuration(duration);
        request.setNotifyCondition(Arrays.asList(notifyCondition));

        SubscribeContextResponse response = iotBrokerClient.subscribeContext(request, URI.create(serverUrl));
        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null && response.getSubscribeError().getStatusCode().getCode()!=200)
            Assert.fail(response.getSubscribeError().getStatusCode().toJsonString());

        String abc="123";
    }

    @Ignore
    @Test
    public void updateSubscribeContextTest() throws DatatypeConfigurationException {
        LOGGER.info("updateSubscribeContextTest");
        String subscriptionId = "f072c-c4a26-6AcC6-c1419-4020b-80b22d917f72407296c";
        //ContextElement sensor = initSensor();
        UpdateContextSubscriptionRequest request = new UpdateContextSubscriptionRequest();
        request.setSubscriptionId(subscriptionId);

        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(new String[]{ "noiseLevel" }), null);
        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONTIMEINTERVAL, Arrays.asList(new String[]{ "noiseLevel" }), null);

        javax.xml.datatype.Duration duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
        request.setDuration(duration);
        request.setNotifyCondition(Arrays.asList(notifyCondition));

        UpdateContextSubscriptionResponse response = iotBrokerClient.updateContextSubscription(request, URI.create(serverUrl));
        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null && response.getSubscribeError().getStatusCode().getCode()!=200)
            Assert.fail(response.getSubscribeError().getStatusCode().toJsonString());
    }

//    @Test
//    public void updateSubscribeContextTest1() throws DatatypeConfigurationException {
//
//        String subscriptionId = "f072c-c4a26-6AcC6-c1419-4020b-80b22d917f72407296c";
//        ContextElement sensor = initSensor();
//        UpdateContextSubscriptionRequest request = new UpdateContextSubscriptionRequest();
//        request.setSubscriptionId(subscriptionId);
//
//        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(new String[]{ "noiseLevel" }), null);
//        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONTIMEINTERVAL, Arrays.asList(new String[]{ "noiseLevel" }), null);
//
//        javax.xml.datatype.Duration duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
//        request.setDuration(duration);
//        request.setNotifyCondition(Arrays.asList(notifyCondition));
//
//        RegisterContextRequest registerContextRequest = new RegisterContextRequest();
//        registerContextRequest.setContextRegistrationList();
//
//        UpdateContextSubscriptionResponse response = iotBrokerClient.registerContext(request, URI.create(serverUrl));
//        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null && response.getSubscribeError().getStatusCode().getCode()!=200)
//            Assert.fail(response.getSubscribeError().getStatusCode().toJsonObject());
//    }

}
