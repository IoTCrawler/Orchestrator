/*-
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
package com.agtinternational.iotcrawler.fiware.clients;
import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.HTTP_REFERENCE_URL;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class EnvVariablesSetter {

    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();


    @Before
    public static void init(){

        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";

        if(!System.getenv().containsKey(NGSILD_BROKER_URL))
            //environmentVariables.set(NGSILD_BROKER_URL, "http://10.67.1.107:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:3000/ngsi-ld/");
            environmentVariables.set(NGSILD_BROKER_URL, "https://mdr.iotcrawler.eu/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://i5-nuc:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://192.168.178.26:9090/ngsi-ld/");
            //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.171:9090/ngsi-ld/");


        environmentVariables.set(HTTP_REFERENCE_URL, "http://10.0.75.1:3001/notify");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:3003/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:9090/ngsi-ld/");
    }
}
