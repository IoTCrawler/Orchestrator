package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
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
import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.core.models.IoTStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_ORCHESTRATOR_URL;


public class OrchestratorRESTClientTests extends OrchestratorTests {

    @Before
    public void init(){
        EnvVariablesSetter.init();
        client = new IoTCrawlerRESTClient(System.getenv().get(IOTCRAWLER_ORCHESTRATOR_URL));
        LOGGER = LoggerFactory.getLogger(OrchestratorRESTClientTests.class);
        try {
            client.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void getStreamsTest() throws Exception {
        super.getStreamsTest();
    }


    @Test
    @Order(4)
    public void getRankedStreamsTest() throws Exception {
        super.getRankedStreamsTest();
    }

    //@Ignore
    @Test
    @Order(6)
    public void getEntitiesTest() throws Exception {
        super.getEntitiesTest();
    }

    @Test
    @Order(6)
    public void getEntitiesWithConversionTest() throws Exception {
        LOGGER.info("getEntitiesTest()");
        List<IoTStream> entities = client.getEntities(IoTStream.class, null, null, 0,0);
        Assert.notNull(entities);
        LOGGER.info(entities.size()+" entities returned");
    }

    //@Ignore
    @Test
    @Order(7)
    public void getStreamByIdTest() throws Exception {
        super.getStreamByIdTest();
    }

    @Test
    @Order(8)
    public void getEntityByIdTest() throws Exception {
       super.getEntityByIdTest();
    }

    //@Ignore
    @Test
    @Order(9)
    public void getAllSensorsTest() throws Exception {
        super.getAllSensorsTest();
    }


}
