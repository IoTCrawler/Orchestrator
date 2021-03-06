package com.agtinternational.iotcrawler.core.ontologies;

/*-
 * #%L
 * core
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



public class IotStream {

    public static final String NS = "http://purl.org/iot/ontology/iot-stream#";
    public static final String Prefix = "iot-stream";

    public static String IotStream = NS+"IotStream";
    public static String StreamObservation = NS+"StreamObservation";
    public static String Context = NS+"Context";

    public static String generatedBy = NS+"generatedBy";
    public static String observes = NS +"observes";
    public static String belongsTo = NS+"belongsTo";
    public static String derivedFrom = NS+"derivedFrom";
    public static String dependsOnContext = NS+"dependsOnContext";

}
