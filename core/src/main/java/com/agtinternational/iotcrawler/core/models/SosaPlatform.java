package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class SosaPlatform extends RDFModel {

    public SosaPlatform(String uri){
        super(uri, getTypeUri());
    }

    public SosaPlatform(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

    public SosaPlatform(String uri, String classURI, String label){
        this(uri, label);
        setType(classURI);
    }

    public SosaPlatform(String uri, Model model){
        super(uri, model);
        setType(SosaPlatform.getTypeUri());
    }

    public static String getTypeUri(){
        //return SosaPlatform.class.getSimpleName();
        return sosaPrefix+":"+"Platform";
    }

    public static SosaPlatform fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new SosaPlatform(rdfModel.getURI(), rdfModel.getModel());
    }

    public static SosaPlatform fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static SosaPlatform fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new SosaPlatform(rdfModel.getURI(), rdfModel.getModel());
    }

//    public static SosaPlatform fromJsonObject(JsonObject jsonObject) {
//        String uri = agtSmartHomeNS +"#Device_"+jsonObject.get("Hostname").hashCode();
//        SosaPlatform ret = new SosaPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }
}