package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class Platform extends RDFModel {


    public Platform(String uri){
        super(uri, getTypeUri());
    }

    public Platform(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

    public Platform(String uri, String classURI, String label){
        this(uri, label);
        setType(classURI);
    }

    public Platform(String uri, Model model){
        super(uri, model);
        setType(Platform.getTypeUri());
    }

    public void hosts(Object value){
        addProperty(SOSA.hosts, value);
    }

    public Object hosts(){
        Object ret = getAttribute(SOSA.hosts);
        return ret;
    }

    public static String getTypeUri(){
        //return SosaPlatform.class.getSimpleName();
        return sosaPrefix+":"+"Platform";
    }

    public static Platform fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new Platform(rdfModel.getURI(), rdfModel.getModel());
    }

    public static Platform fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static Platform fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new Platform(rdfModel.getURI(), rdfModel.getModel());
    }

//    public static SosaPlatform fromJsonObject(JsonObject jsonObject) {
//        String uri = agtSmartHomeNS +"#Device_"+jsonObject.get("Hostname").hashCode();
//        SosaPlatform ret = new SosaPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }
}