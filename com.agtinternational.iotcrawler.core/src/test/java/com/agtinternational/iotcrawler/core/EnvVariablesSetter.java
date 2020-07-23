package com.agtinternational.iotcrawler.core;/*-
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
import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_ORCHESTRATOR_URL;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;


public class EnvVariablesSetter {

    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init(){

        //for inserting test entities
        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            environmentVariables.set(NGSILD_BROKER_URL, "http://i5-nuc:9090/ngsi-ld/");

        //for all get requests
        if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://192.168.0.85:3001/ngsi-ld/");


        //for getting notifications
        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "localhost:5672");


        //for getting streamObservation of targeted streams
        if(!System.getenv().containsKey(IOTCRAWLER_GRAPHQL_ENDPOINT))
            environmentVariables.set(IOTCRAWLER_GRAPHQL_ENDPOINT, "http://localhost:8080/graphql");



    }
}