package com.agtinternational.iotcrawler.fiware.clients;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.orange.ngsi2.model.Paginated;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.annotation.Order;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class NgsiLDClientManualTests {

    private NgsiLDClient ngsiLdClient;
    String serverUrl;

    @Before
    public void init(){
        EnvVariablesSetter.init();

        if (System.getenv().containsKey(NGSILD_BROKER_URL))
            serverUrl = System.getenv(NGSILD_BROKER_URL);

        ngsiLdClient = new NgsiLDClient(serverUrl);

    }

    @Order(3)
    @Test
    public void getEntityByIdTest() throws Exception {
        EntityLD entityLD = ngsiLdClient.getEntitySync("urn:ngsi-ld:Stream:ora10",null,null);
        String abc = "asd";
    }

    @Order(3)
    @Test
    public void getEntitiesByIdTest() throws Exception {
        List<String> ids = Arrays.asList(new String[]{"urn:ngsi-ld:Stream:ora10"});
        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(ids,null,null,null,null,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }

}
