package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.orange.ngsi2.model.Entity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;
import com.agtinternational.iotcrawler.core.Constants;

import static com.agtinternational.iotcrawler.core.Constants.sosaNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;

public class Sensor extends RDFModel {

    public Sensor(String uri){
        super(uri, getTypeUri());
    }

    public Sensor(String uri, Model model){
        super(uri, model);
    }


    public Sensor(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }


    public static String getTypeUri(){
        return sosaPrefix+":"+"Sensor";
        //return Sensor.class.getSimpleName();
        //return "sosa:Sensor";

    }

    public static Sensor fromEntity(EntityLD entity) throws Exception {
        //entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new Sensor(rdfModel.getURI(), rdfModel.getModel());
    }

//    public static Sensor fromJsonObject(JsonObject jsonObject) {
//        String uri = agtSmartHome+"#Device_"+jsonObject.get("Hostname").hashCode();
//        IoTPlatform ret = new IoTPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }



}