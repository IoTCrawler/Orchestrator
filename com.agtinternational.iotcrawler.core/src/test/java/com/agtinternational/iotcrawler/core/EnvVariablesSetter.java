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


public class EnvVariablesSetter {

    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init(){

        if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://localhost:3001/ngsi-ld/");
            //environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://10.67.1.107:9090/ngsi-ld/");

        if(!System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "localhost:5672");
        //environmentVariables.set(IOTCRAWLER_RABBIT_HOST, "rabbit");

        if(!System.getenv().containsKey(HTTP_REFERENCE_URL))
            environmentVariables.set(HTTP_REFERENCE_URL, "http://10.0.75.1:3001/notify");

        if(!System.getenv().containsKey(IOTCRAWLER_GRAPHQL_ENDPOINT))
            environmentVariables.set(IOTCRAWLER_GRAPHQL_ENDPOINT, "http://localhost:8080/graphql");

    }
}