package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

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

    public void observes(Object value){
        addProperty(SOSA.observes, value);
    }

    public String observes(){
        String ret = (String)getAttribute(SOSA.observes);
        return ret;
    }

    public void isHostedBy(Object value){
        addProperty(SOSA.isHostedBy, value);
    }

    public String isHostedBy(){
        String ret = (String)getAttribute(SOSA.isHostedBy);
        return ret;
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
//        SosaPlatform ret = new SosaPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }



}