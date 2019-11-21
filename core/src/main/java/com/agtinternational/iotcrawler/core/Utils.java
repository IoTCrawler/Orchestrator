package com.agtinternational.iotcrawler.core;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.*;
import org.apache.commons.io.Charsets;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    static Logger LOGGER = LoggerFactory.getLogger(Utils.class);
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

    public static String prettyPrint(String jsonString){
        JsonElement parsed = new JsonParser().parse(jsonString);
        return gson.toJson(parsed);
    }

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

    public static String getTypeURI(Class targetClass){
        String type = null;
        if(targetClass.equals(IoTStream.class))
            type = IoTStream.getTypeUri();
        else if(targetClass.equals(Sensor.class))
            type = Sensor.getTypeUri();
        else if(targetClass.equals(Platform.class))
            type = Platform.getTypeUri();

        else if(targetClass.equals(ObservableProperty.class))
            type = ObservableProperty.getTypeUri();
        else {
            LOGGER.error("No suitable type for entity " + targetClass.getSimpleName());
        }
        return type;
    }

    public static Class getTargetClass(String typeURI){
        Class ret = null;
        if(typeURI.equals(IoTStream.getTypeUri()))
            ret = IoTStream.class;
        else if(typeURI.equals(Sensor.getTypeUri()))
            ret =  Sensor.class;
        else if(typeURI.equals(Platform.getTypeUri()))
            ret = Platform.class;

        else if(typeURI.equals(ObservableProperty.getTypeUri()))
            ret = ObservableProperty.class;
        else {
            LOGGER.error("No suitable class for  " + typeURI);
        }
        return ret;
    }

    public static <T> List<T> convertEntitiesToType(Iterable<EntityLD> entities, Class<T> targetClass) throws Exception {
        List<T> ret = new ArrayList<>();

        if(entities!=null)
            for(EntityLD entity: entities){
                T toAdd=null;
                try {
                    if(targetClass.equals(IoTStream.class))
                        toAdd = (T)IoTStream.fromEntity(entity);

                    if(targetClass.equals(Sensor.class))
                        toAdd = (T)Sensor.fromEntity(entity);

                    if(targetClass.equals(Platform.class))
                        toAdd = (T) Platform.fromEntity(entity);

                    if(targetClass.equals(ObservableProperty.class))
                        toAdd = (T)ObservableProperty.fromEntity(entity);

                    if(toAdd!=null)
                        ret.add(toAdd);
                    else
                        throw new Exception("No suitable type for entity " + targetClass.getSimpleName());

                } catch (Exception e) {
                    LOGGER.error("Failed to create "+targetClass.getSimpleName()+" from {}: {}", entity.getId(), e.getLocalizedMessage());
                }

            }

        return ret;
    }

    public static JsonObject parseJsonQuery(String query){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(query);
        for(String key : jsonObject.keySet()){
            if(SOSA.observes.toLowerCase().endsWith(key.toLowerCase())) {
                JsonElement value = jsonObject.get(key);
                jsonObject.remove(key);
                jsonObject.add(SOSA.observes, value);
            }

            if(SOSA.madeBySensor.toLowerCase().endsWith(key.toLowerCase())) {
                JsonElement value = jsonObject.get(key);
                jsonObject.remove(key);
                jsonObject.add(SOSA.madeBySensor, value);
            }
        }
        return jsonObject;
    }
}
