package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.sosaNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;


public class ObservableProperty extends RDFModel {

    public ObservableProperty(String uri){
        super(uri, getTypeUri());
    }

    public ObservableProperty(String uri, String classURI){
        super(uri, classURI);
    }

    public ObservableProperty(String uri, Model model){
        super(uri, model);
    }

    public ObservableProperty(String uri, String classURI, String label){
        this(uri, classURI);
        addProperty(RDFS.label, label);
    }

    public static String getTypeUri() {
        //return ObservableProperty.class.getSimpleName();
        //return "sosa:ObservableProperty";
        return sosaPrefix+":"+"ObservableProperty";
    }

    public static ObservableProperty fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new ObservableProperty(rdfModel.getURI(), rdfModel.getModel());
    }

}
