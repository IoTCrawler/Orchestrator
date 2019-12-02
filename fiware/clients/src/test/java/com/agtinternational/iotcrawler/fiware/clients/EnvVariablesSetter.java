/*-
 * #%L
 * fiware-clients
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
package com.agtinternational.iotcrawler.fiware.clients;
import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class EnvVariablesSetter {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void init(){
        //public static final String IoTCTripleStoreURI = "http://10.67.42.53:10035/repositories/IoTCrawler2/sparql";
        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";


        environmentVariables.set(NGSILD_BROKER_URL, "http://djane:3000/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:3001/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:3003/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:9090/ngsi-ld/");
    }
}
