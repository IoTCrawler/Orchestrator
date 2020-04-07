package com.agtinternational.iotcrawler.core.ontologies;

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


public class SOSA {

    public static final String NS = "http://www.w3.org/ns/sosa/";
    public static final String Prefix = "sosa";


    public static String madeObservation = NS +"madeObservation";
    public static String madeBySensor = NS +"madeBySensor";

    public static String hosts = NS +"hosts";
    public static String isHostedBy = NS +"isHostedBy";

    public static String observes = NS +"observes";
    public static String isObservedBy = NS +"isObservedBy";

    public static String platform = NS +"Platform";
    public static String sensor = NS +"Sensor";
    public static String observableProperty = NS +"ObservableProperty";

    public static String hasResult = NS +"hasResult";
    public static String resultTime = NS +"resultTime";


//    static {
//        M
//    }
}
