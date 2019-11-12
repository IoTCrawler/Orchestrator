package com.agtinternational.iotcrawler.orchestrator;

import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_RABBIT_HOST;
import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_REDIS_HOST;
import static com.agtinternational.iotcrawler.orchestrator.Constants.*;

public class EnvVariablesSetter {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void init(){
        //public static final String IoTCTripleStoreURI = "http://10.67.42.53:10035/repositories/IoTCrawler2/sparql";
        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";

        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            //environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "localhost");
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "rabbit");

        if(!System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            environmentVariables.set(IOTCRAWLER_REDIS_HOST,  "redis");

//        environmentVariables.set(Constants.TRIPLE_STORE_URI, "http://localhost:10035/repositories/MDR/sparql");
//        environmentVariables.set(Constants.TRIPLE_STORE_USER, "test");
//        environmentVariables.set(Constants.TRIPLE_STORE_PASS, "xyzzy");

        if(!System.getenv().containsKey(NGSILD_BROKER_URI))
            environmentVariables.set(Constants.NGSILD_BROKER_URI, "http://djane:3000/ngsi-ld/");
            //environmentVariables.set(Constants.NGSILD_BROKER_URI, "http://localhost:3000/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URI, "http://localhost:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URI, "http://155.54.95.248:9090/ngsi-ld/");

        if(!System.getenv().containsKey(IOT_BROKER_URI))
            //environmentVariables.set(Constants.IOT_BROKER_URI, "http://10.67.1.107:8060/ngsi10");
            environmentVariables.set(IOT_BROKER_URI, "http://iotbroker:8060/ngsi10");

        if(!System.getenv().containsKey(HTTP_SERVER_HOST))
            environmentVariables.set(HTTP_SERVER_HOST, "10.67.1.107");

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
