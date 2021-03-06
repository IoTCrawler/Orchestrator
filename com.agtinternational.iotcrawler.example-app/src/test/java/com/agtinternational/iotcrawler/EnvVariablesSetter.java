package com.agtinternational.iotcrawler;

/*-
 * #%L
 * example-app
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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


import com.agtinternational.iotcrawler.core.Constants;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_ORCHESTRATOR_URL;


public class EnvVariablesSetter {

    public final static EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void init() throws Exception {

        if(!System.getenv().containsKey(IOTCRAWLER_ORCHESTRATOR_URL))
            environmentVariables.set(IOTCRAWLER_ORCHESTRATOR_URL, "http://localhost:3001/ngsi-ld/");

        environmentVariables.set(Constants.IOTCRAWLER_RABBIT_HOST, "localhost:5672");
        //environmentVariables.set(Constants.IOTCRAWLER_RABBIT_HOST, "35.195.190.161:5672");
    }
}
