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


import com.agtinternational.iotcrawler.core.interfaces.IoTCrawlerClient;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.agtinternational.iotcrawler.core.Constants.CUT_TYPE_URIS;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrchestratorManualTests {

    protected Logger LOGGER = LoggerFactory.getLogger(OrchestratorTests.class);

    protected IoTCrawlerClient ioTCrawlerClient;
    Boolean cutURIs;
    NgsiLDClient ngsiLDClient;

    @Before
    public void init(){
        EnvVariablesSetter.init();
        //EnvVariablesSetterRemote.init();

        cutURIs = (System.getenv().containsKey(CUT_TYPE_URIS)?Boolean.parseBoolean(System.getenv(CUT_TYPE_URIS)):false);
        if(ioTCrawlerClient ==null)
            ioTCrawlerClient = new Orchestrator(cutURIs);

        try {
            ioTCrawlerClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ngsiLDClient = new NgsiLDClient(System.getenv(NGSILD_BROKER_URL));
    }

    @Test
    public void runOrchestrator() throws Exception {
        LOGGER.info("Running orchestrator in test mode");
        ioTCrawlerClient.run();
        String test = "123";
    }



}

