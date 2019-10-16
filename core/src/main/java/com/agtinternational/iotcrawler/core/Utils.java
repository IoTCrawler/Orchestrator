package com.agtinternational.iotcrawler.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.commons.io.Charsets;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;

public class Utils {


//    public static Model createModelFromProperties(String uri, List<Pair<RDFNode, RDFNode>> properties){
//        Model model = ModelFactory.createDefaultModel();
//        Resource resource = model.createResource(uri);
//        for (Pair<RDFNode, RDFNode> pair : properties){
//            Property property = (pair.getKey().asResource().getURI().equals(RDF.type.getURI())? RDF.type: model.createProperty(pair.getKey().asResource().getURI()));
//            try {
//                model.add(resource, property, pair.getValue());
//                //model.add(resource, property, "123");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return model;
//    }

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String prettyPrint(JsonElement jsonElement){
        return gson.toJson(jsonElement);
    }

    public static String writeModel2JsonLDString(Model model) {
        if (model == null) {
            return "";
        } else {
            StringWriter writer = new StringWriter();
            RDFDataMgr.write(writer, model, Lang.JSONLD);
            return writer.toString();
        }
    }

    public static String writeModel2RDFString(Model model) {
        if (model == null) {
            return "";
        } else {
            StringWriter writer = new StringWriter();
            RDFDataMgr.write(writer, model, Lang.RDFXML);
            return writer.toString();
        }
    }


    public static Model readModel(byte data[]) {
        return readModel(data, 0, data.length);
    }


    public static Model readModel(byte data[], int offset, int length) {
        return readModel(readString(data, offset, length));
    }

    public static Model readModel(String string) {
        StringReader reader = new StringReader(string);
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, reader, "", Lang.JSONLD);
        //InputStream inputStream = new ByteArrayInputStream(string.getBytes());
        //RDFDataMgr.read(model, inputStream, Lang.JSONLD);
        String ret = Utils.writeModel2JsonLDString(model);
        return model;
    }

    public static String readString(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        } else {
            return new String(data, offset, length, Charsets.UTF_8);
        }
    }

    public static String getFragment(String uriString){
        URI uri = URI.create(uriString);
        String ret = uri.getFragment();
        if(ret==null) {
            String[] splitted = uriString.split("/");
            ret = splitted[splitted.length-1];
        }
        return ret;
    }

    public static String getNamespace(String uriString){
        URI uri = URI.create(uriString);
        String ret = uri.getFragment();
        if(ret==null) {
            String[] splitted = uriString.split("/");
            ret = splitted[splitted.length-1];
        }
        return ret;
    }


}
