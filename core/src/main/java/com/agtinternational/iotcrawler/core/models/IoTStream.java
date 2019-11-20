package com.agtinternational.iotcrawler.core.models;

//import com.agtinternational.iotcrawler.core.LinkFilter;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;

import static com.agtinternational.iotcrawler.core.Constants.*;

public class IoTStream extends RDFModel {

    //public static String madeBySensor = sosaNS+"madeBySensor";

    public IoTStream(String uri){

        super(uri, getTypeUri());
    }

    public IoTStream(String uri, String label){

        this(uri);
        addProperty(RDFS.label, label);

    }


    public IoTStream(String uri, Model model){
        super(uri, IoTStream.getTypeUri(), model);
    }

    public static String getTypeUri(){
        //return "iotc:IoTStream";
        return iotcPrefix+":"+IoTStream.class.getSimpleName();
    }

    public static IoTStream fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new IoTStream(rdfModel.getURI(), rdfModel.getModel());
    }


    public static IoTStream fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static IoTStream fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new IoTStream(rdfModel.getURI(), rdfModel.getModel());
    }

//    public ObservableProperty getObservableProperty(){
//        return new ObservableProperty("http://Uri");
//    }

    public String madeBySensor(){
        return (String)getAttribute(SOSA.madeBySensor);
    }



}
