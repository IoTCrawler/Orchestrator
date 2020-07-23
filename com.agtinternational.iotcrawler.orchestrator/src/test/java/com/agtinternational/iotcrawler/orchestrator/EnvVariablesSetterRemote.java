package com.agtinternational.iotcrawler.orchestrator;

import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.*;
import static com.agtinternational.iotcrawler.core.Constants.HTTP_REFERENCE_URL;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
import static com.agtinternational.iotcrawler.orchestrator.Constants.HTTP_SERVER_HOST;
import static com.agtinternational.iotcrawler.orchestrator.Constants.HTTP_SERVER_PORT;

public class EnvVariablesSetterRemote {
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init(){
        //public static final String IoTCTripleStoreURI = "http://10.67.42.53:10035/repositories/IoTCrawler2/sparql";
        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";



//        if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
//            //environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://localhost:3001/ngsi-ld/");
//            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://10.67.1.107:9090/ngsi-ld/");
//


        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "35.241.162.84");



        if(!System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            environmentVariables.set(IOTCRAWLER_REDIS_HOST,  "redis");

//        environmentVariables.set(Constants.TRIPLE_STORE_URI, "http://localhost:10035/repositories/MDR/sparql");
//        environmentVariables.set(Constants.TRIPLE_STORE_USER, "test");
//        environmentVariables.set(Constants.TRIPLE_STORE_PASS, "xyzzy");

        //environmentVariables.set(CUT_TYPE_URIS, "false");
        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            environmentVariables.set(NGSILD_BROKER_URL, "http://i5-nuc:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");


        //if(!System.getenv().containsKey(RANKING_COMPONENT_URL))
        //    environmentVariables.set(RANKING_COMPONENT_URL, "https://ranking.iotcrawler.eu/ngsi-ld/");


//        if(!System.getenv().containsKey(IOT_BROKER_URL))
//            //environmentVariables.set(Constants.IOT_BROKER_URI, "http://10.67.1.107:8060/ngsi10");
//            environmentVariables.set(IOT_BROKER_URL, "http://iotbroker:8060/ngsi10");


        if(!System.getenv().containsKey(HTTP_SERVER_HOST))
            environmentVariables.set(HTTP_SERVER_HOST, "192.168.0.85");

        if(!System.getenv().containsKey(HTTP_SERVER_PORT))
            environmentVariables.set(HTTP_SERVER_PORT, "3001");

        if(!System.getenv().containsKey(HTTP_REFERENCE_URL))
            environmentVariables.set(HTTP_REFERENCE_URL, "http://staging.orchestrator.iotcrawler.eu/notify");
        //environmentVariables.set(HTTP_REFERENCE_URL, "http://"+System.getenv(HTTP_SERVER_HOST)+":"+System.getenv(HTTP_SERVER_PORT)+"/notify");


        //public static final String ContextBrokerEndpoint = "http://localhost:1026/";

        //public final static String IoTBrokerEndpoint = "http://10.67.1.41:8060/ngsi10";

        ////public static final String CONTEXT_BROKER_URL = "http://localhost:1026/";
        //public static final String CONTEXT_BROKER_URL = "http://155.54.210.177:1026";

        //public final static String IOT_BROKER_URL = "http://155.54.95.237:8060/ngsi10";
        //public final static String IOT_BROKER_URL = "http://155.54.210.176:8060/ngsi10";
        //environmentVariables.set(TRIPLE_STORE_PASS, "xyzzy");
    }
}
