package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_RABBIT_HOST;
import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_REDIS_HOST;
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
            //environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "localhost");
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "rabbit");

        if(!System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            environmentVariables.set(IOTCRAWLER_REDIS_HOST,  "redis");

//        environmentVariables.set(Constants.TRIPLE_STORE_URI, "http://localhost:10035/repositories/MDR/sparql");
//        environmentVariables.set(Constants.TRIPLE_STORE_USER, "test");
//        environmentVariables.set(Constants.TRIPLE_STORE_PASS, "xyzzy");

        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            environmentVariables.set(NGSILD_BROKER_URL, "http://djane:3000/ngsi-ld/");

//        if(!System.getenv().containsKey(RANKING_COMPONENT_URI))
//            environmentVariables.set(RANKING_COMPONENT_URI, "http://localhost:3003/ngsi-ld/");

            //environmentVariables.set(Constants.NGSILD_BROKER_URI, "http://localhost:3003/ngsi-ld/");
            //environmentVariables.set(Constants.NGSILD_BROKER_URI, "http://localhost:3000/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URI, "http://localhost:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URI, "http://155.54.95.248:9090/ngsi-ld/");

        if(!System.getenv().containsKey(IOT_BROKER_URL))
            //environmentVariables.set(Constants.IOT_BROKER_URI, "http://10.67.1.107:8060/ngsi10");
            environmentVariables.set(IOT_BROKER_URL, "http://iotbroker:8060/ngsi10");

        //if(!System.getenv().containsKey(HTTP_SERVER_HOST))
        //    environmentVariables.set(HTTP_SERVER_HOST, "10.67.1.107");

        if(!System.getenv().containsKey(HTTP_SERVER_PORT))
            environmentVariables.set(HTTP_SERVER_PORT, "3001");

        //public static final String ContextBrokerEndpoint = "http://localhost:1026/";

        //public final static String IoTBrokerEndpoint = "http://10.67.1.41:8060/ngsi10";

        ////public static final String CONTEXT_BROKER_URL = "http://localhost:1026/";
        //public static final String CONTEXT_BROKER_URL = "http://155.54.210.177:1026";

        //public final static String IOT_BROKER_URL = "http://155.54.95.237:8060/ngsi10";
        //public final static String IOT_BROKER_URL = "http://155.54.210.176:8060/ngsi10";
        //environmentVariables.set(TRIPLE_STORE_PASS, "xyzzy");
    }
}
