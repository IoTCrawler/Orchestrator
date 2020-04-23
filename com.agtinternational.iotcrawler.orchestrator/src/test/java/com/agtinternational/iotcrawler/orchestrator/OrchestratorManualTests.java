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
import com.agtinternational.iotcrawler.core.interfaces.IotCrawlerClient;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
//import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreMDRClient;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.subscription.Endpoint;
import com.agtinternational.iotcrawler.fiware.models.subscription.EntityInfo;
import com.agtinternational.iotcrawler.fiware.models.subscription.NotificationParams;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
import com.google.gson.JsonObject;
import com.orange.ngsi2.model.Condition;
import com.orange.ngsi2.model.SubjectEntity;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.apache.http.entity.ContentType;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.CUT_TYPE_URIS;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
import static com.agtinternational.iotcrawler.orchestrator.Constants.HTTP_REFERENCE_URL;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrchestratorManualTests {

    protected Logger LOGGER = LoggerFactory.getLogger(OrchestratorTests.class);

    protected IotCrawlerClient client;
    Boolean cutURIs;
    NgsiLDClient ngsiLDClient;

    @Before
    public void init(){
        EnvVariablesSetter.init();

        cutURIs = (System.getenv().containsKey(CUT_TYPE_URIS)?Boolean.parseBoolean(System.getenv(CUT_TYPE_URIS)):false);
        if(client==null)
            client = new Orchestrator(cutURIs);

        try {
            client.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ngsiLDClient = new NgsiLDClient(System.getenv(NGSILD_BROKER_URL));
    }

    @Test
    public void runOrchestrator() throws Exception {
        LOGGER.info("Running orchestrator in test mode");
        client.run();
        String test = "123";
    }



}

