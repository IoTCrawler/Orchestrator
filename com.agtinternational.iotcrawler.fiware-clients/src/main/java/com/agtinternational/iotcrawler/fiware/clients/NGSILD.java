package com.agtinternational.iotcrawler.fiware.clients;

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

//import com.orange.ngsi2.model.Entity;

public class NGSILD {
    //public static String NS = "http://uri.etsi.org/ngsi-ld/";
    public static String CORE_CONTEXT ="https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld";
    public static String NS = "";
    public static String value = NS+"hasValue";
    public static String Property = NS+"Property";
    public static String Relationship = NS+"Relationship";

    //"DateTime= NS+"DateTime";
    public static String Date= NS+"Date";
    public static String Time= NS+"Time";

//    public static String createdAt": {
//    public static String @id= NS+"createdAt";
//    public static String @type": "DateTime"
//    },
//    public static String modifiedAt": {
//    public static String @id= NS+"modifiedAt";
//    public static String @type": "DateTime"
//            },
    //public static Entity observedAt = new Entity(NS+"observedAt", "DateTime");
//    public static String @id= NS+"observedAt";
//    public static String @type": "DateTime"
//            },
    public static String unitCode= NS+"unitCode";
    public static String location= NS+"location";
    public static String observationSpace= NS+"observationSpace";
    public static String operationSpace= NS+"operationSpace";
    public static String GeoProperty= NS+"GeoProperty";
    public static String TemporalProperty= NS+"TemporalProperty";
    public static String ContextSourceRegistration= NS+"ContextSourceRegistration";
    public static String Subscription= NS+"Subscription";
    public static String Notification= NS+"Notification";
    public static String ContextSourceNotification= NS+"ContextSourceNotification";
    public static String title= NS+"title";
    public static String detail= NS+"detail";
    public static String idPattern= NS+"idPattern";
    public static String name= NS+"name";
    public static String description= NS+"description";
    public static String information= NS+"information";
    public static String timestamp= NS+"timestamp";
}
