package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.core.Constants;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.orange.ngsi2.model.Entity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class IoTPlatform extends RDFModel {

    public IoTPlatform(String uri){
        super(uri, getTypeUri());
    }

    public IoTPlatform(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

    public IoTPlatform(String uri, String classURI, String label){
        this(uri, classURI);
        addProperty(RDFS.label, label);
    }

    public IoTPlatform(String uri, Model model){
        super(uri, model);
    }

    public static String getTypeUri(){
        //return IoTPlatform.class.getSimpleName();
        return sosaPrefix+":"+"Platform";
    }

    public static IoTPlatform fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new IoTPlatform(rdfModel.getURI(), rdfModel.getModel());
    }

    public static IoTPlatform fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static IoTPlatform fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new IoTPlatform(rdfModel.getURI(), rdfModel.getModel());
    }

//    public static IoTPlatform fromJsonObject(JsonObject jsonObject) {
//        String uri = agtSmartHomeNS +"#Device_"+jsonObject.get("Hostname").hashCode();
//        IoTPlatform ret = new IoTPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }
}