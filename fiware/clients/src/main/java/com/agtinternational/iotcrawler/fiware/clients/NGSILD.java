package com.agtinternational.iotcrawler.fiware.clients;

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
