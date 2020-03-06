package com.agtinternational.iotcrawler.core;

/*-
 * #%L
 * core
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

import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class EnvVariablesSetter {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void init(){

        environmentVariables.set(Constants.IOTCRAWLER_RABBIT_HOST, "rabbit");
        environmentVariables.set(Constants.IOTCRAWLER_ORCHESTRATOR_URL, "http://localhost:3001/ngsi-ld/");

    }
}