package com.agtinternational.iotcrawler.fiware.clients;/*-
 * #%L
 * fiware-clients
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
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class NgsiLDClientTest_Remote extends NgsiLDClientTestsAutomated {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();


    @Before
    public void init(){
        environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://metadata-repository-scorpiobroker.35.241.228.250.nip.io/ngsi-ld/");

        super.init();
    }

    public NgsiLDClientTest_Remote(EntityLD entityLD) {
        super(entityLD);
    }


}
