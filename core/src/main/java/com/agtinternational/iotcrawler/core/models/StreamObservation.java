package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.agtinternational.iotcrawler.core.Constants.iotcPrefix;


public class StreamObservation extends RDFModel {
    private static Logger LOGGER = LoggerFactory.getLogger(StreamObservation.class);

//    public StreamObservation(String uri, HashMap attributes){
//        super(URI.create(uri), URI.create(Constants.observationURI));
//    }

    public StreamObservation(String uri) {
        super(uri,  getTypeUri());
    }

    public StreamObservation(String uri, Model model){
        super(uri, model);

    }

    public static String getTypeUri(){
        //return StreamObservation.class.getSimpleName();
        //return "iotc:StreamObservation";
        return iotcPrefix+":"+"StreamObservation";
    }


    public static StreamObservation fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static StreamObservation fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromContextElement(ContextElement contextElement){
        RDFModel rdfModel = RDFModel.fromContextElement(contextElement);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromJsonObject(String uri, String basicNS, JsonObject jsonObject) {
        StreamObservation ret = new StreamObservation(uri);
        for(String key: jsonObject.keySet()) {
            try {
                ret.addProperty(basicNS + key, jsonObject.get(key));
            }
            catch (Exception e){
                e.printStackTrace();
                LOGGER.error("Failed to add property: {}", key);
            }
        }
        return ret;
    }

//    public String getBelongsToStreamUri(){
//        Property property = model.createProperty(belongsTo);
//        if(!model.listObjectsOfProperty(property).hasNext())
//            return null;
//        String ret = model.listObjectsOfProperty(property).next().asResource().getURI();
//        return ret;
//    }



}
