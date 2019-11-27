package com.agtinternational.iotcrawler.fiware.clients;

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

//import com.orange.ngsi2.model.Entity;

public class NGSILD {
    //public static String NS = "http://uri.etsi.org/ngsi-ld/";
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
