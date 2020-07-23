package com.agtinternational.iotcrawler.core;

import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.*;
import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_GRAPHQL_ENDPOINT;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class EnvVariablesSetterRemote {
    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init(){

        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");

         if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "https://staging.orchestrator.iotcrawler.eu/");


        //for getting notifications
        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "35.241.162.84:5672");


        //for getting streamObservation of targeted streams
        if(!System.getenv().containsKey(IOTCRAWLER_GRAPHQL_ENDPOINT))
            environmentVariables.set(IOTCRAWLER_GRAPHQL_ENDPOINT, "https://search-enabler.iotcrawler.eu/graphql");


    }
}
