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

import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.*;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
import static com.agtinternational.iotcrawler.orchestrator.Constants.*;


public class EnvVariablesSetter {

    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init(){
        //public static final String IoTCTripleStoreURI = "http://10.67.42.53:10035/repositories/IoTCrawler2/sparql";
        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";



        if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://localhost:3001/ngsi-ld/");


        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "localhost:5672");
            //environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "rabbit");



        if(!System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            environmentVariables.set(IOTCRAWLER_REDIS_HOST,  "redis");

//        environmentVariables.set(Constants.TRIPLE_STORE_URI, "http://localhost:10035/repositories/MDR/sparql");
//        environmentVariables.set(Constants.TRIPLE_STORE_USER, "test");
//        environmentVariables.set(Constants.TRIPLE_STORE_PASS, "xyzzy");

        environmentVariables.set(CUT_TYPE_URIS, "false");
        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:3000/ngsi-ld/");
                environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.171:9090/ngsi-ld/");



//        if(!System.getenv().containsKey(IOT_BROKER_URL))
//            //environmentVariables.set(Constants.IOT_BROKER_URI, "http://10.67.1.107:8060/ngsi10");
//            environmentVariables.set(IOT_BROKER_URL, "http://iotbroker:8060/ngsi10");


        if(!System.getenv().containsKey(HTTP_SERVER_HOST))
            environmentVariables.set(HTTP_SERVER_HOST, "10.67.1.107");

        if(!System.getenv().containsKey(HTTP_SERVER_PORT))
            environmentVariables.set(HTTP_SERVER_PORT, "3001");

        if(!System.getenv().containsKey(HTTP_REFERENCE_URL))
            environmentVariables.set(HTTP_REFERENCE_URL, "http://"+System.getenv(HTTP_SERVER_HOST)+":"+System.getenv(HTTP_SERVER_PORT)+"/notify");


        //public static final String ContextBrokerEndpoint = "http://localhost:1026/";

        //public final static String IoTBrokerEndpoint = "http://10.67.1.41:8060/ngsi10";

        ////public static final String CONTEXT_BROKER_URL = "http://localhost:1026/";
        //public static final String CONTEXT_BROKER_URL = "http://155.54.210.177:1026";

        //public final static String IOT_BROKER_URL = "http://155.54.95.237:8060/ngsi10";
        //public final static String IOT_BROKER_URL = "http://155.54.210.176:8060/ngsi10";
        //environmentVariables.set(TRIPLE_STORE_PASS, "xyzzy");
    }
}