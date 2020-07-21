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
import com.orange.ngsi2.model.Paginated;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.annotation.Order;

import java.nio.file.Paths;
import java.util.*;

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
        EntityLD entityLD = ngsiLdClient.getEntitySync("urn:ngsi-ld:IndoorTemperatureSensor_1",null,null);
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
        List<String> ids = Arrays.asList(new String[]{"urn:ngsi-ld:Stream:ora10"});
        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(ids,null,null,null,null,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }

    @Order(3)
    @Test
    public void getEntitiesByParameterTest() throws Exception {
        Map query = new HashMap<String, String>();
        //query.put("http://www.w3.org/2000/01/rdf-schema#label", "\"homee_00055110D732\"");
        //query.put("http://www.agtinternational.com/ontologies/ngsi-ld#alternativeType","\"http://agtinternational/smartHomeApp#HouseholdStateStream\"");
        query.put("http://dummyurl/speed","\"55\"");

        Collection<String> types = Arrays.asList(new String[]{
                "http://purl.org/iot/ontology/iot-stream#IotStream"
                //"http://www.w3.org/ns/sosa/Platform"
        });

        Paginated<EntityLD> ret = ngsiLdClient.getEntitiesSync(null,null,types,null,query,null,null, 0,0, false);
        List<EntityLD> entities = ret.getItems();
        String abc = "asd";
    }



}
