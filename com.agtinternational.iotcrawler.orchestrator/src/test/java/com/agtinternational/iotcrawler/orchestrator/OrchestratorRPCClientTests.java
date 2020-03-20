package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
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

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.clients.IoTCrawlerRESTClient;
import com.agtinternational.iotcrawler.core.clients.IoTCrawlerRPCClient;
import com.agtinternational.iotcrawler.core.models.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_RABBIT_HOST;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrchestratorRPCClientTests extends OrchestratorTests {

    //private Logger LOGGER = LoggerFactory.getLogger(OrchestratorRPCClientTests.class);

    @Before
    public void init(){
        EnvVariablesSetter.init();
        client = new IoTCrawlerRPCClient(System.getenv(IOTCRAWLER_RABBIT_HOST));
        LOGGER = LoggerFactory.getLogger(OrchestratorRPCClientTests.class);
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


    @Order(10)
    @Test
    public void getObservationsTest() throws Exception {
        super.getObservationsTest();
    }




}
