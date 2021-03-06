package com.agtinternational.iotcrawler.core;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.core.ontologies.SOSA;
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
import java.util.Map;

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

    public static <T> List<T> convertEntitiesToTargetClass(Iterable<EntityLD> entities, Class<T> targetClass) throws Exception {
        List<T> ret = new ArrayList<>();

        if(entities!=null)
            for(EntityLD entity: entities) {
                try {
                    T targetEnt = (T) convertEntityToTargetClass(entity, targetClass);
                    ret.add(targetEnt);
                } catch (Exception e){
                    LOGGER.error("Failed to convert to target class {}", e.getLocalizedMessage());
                }
            }
        return ret;
    }

    public static <T> Object convertEntityToTargetClass(EntityLD entity, Class<T> targetClass){

            T toAdd=null;
            try {
                if(targetClass.equals(IoTStream.class))
                    toAdd = (T)IoTStream.fromEntity(entity);

                else if(targetClass.equals(Sensor.class))
                    toAdd = (T)Sensor.fromEntity(entity);

                else if(targetClass.equals(Platform.class))
                    toAdd = (T) Platform.fromEntity(entity);

                else if(targetClass.equals(ObservableProperty.class))
                    toAdd = (T)ObservableProperty.fromEntity(entity);
                else
                    throw new Exception("No suitable type for entity " + targetClass.getSimpleName());

            } catch (Exception e) {
                LOGGER.error("Failed to create "+targetClass.getSimpleName()+" from {}: {}", entity.getId(), e.getLocalizedMessage());
                e.printStackTrace();
            }
        return toAdd;

    }

    public static String cutURL(String inputURL, Map<String, String> namespaces){

        String URLNameSpace = ModelFactory.createDefaultModel().createProperty(inputURL).getNameSpace();
        String fragment = (inputURL.startsWith(URLNameSpace)? inputURL.substring(URLNameSpace.length()) : Utils.getFragment(inputURL));

        String nsKey = null;
        if (URLNameSpace.endsWith(":")) { //ns is a prefix
            nsKey = URLNameSpace.substring(0, URLNameSpace.length() - 1);
        } else {//ns is a full URI
            if (!namespaces.containsValue(URLNameSpace)) {
                String[] splitted = URI.create(URLNameSpace).getPath().split("/");
                nsKey = splitted[splitted.length - 1];
                namespaces.put(nsKey, URLNameSpace);
            } else
                nsKey = findNsByValue(URLNameSpace, namespaces);
        }
        inputURL = nsKey+":"+fragment;
        return inputURL;
    }

    private static String findNsByValue(String value, Map<String, String> namespaces){
        for(String nsKey: namespaces.keySet())
            if(namespaces.get(nsKey).equals(value))
                return nsKey;
        return null;
    }

    public static JsonObject parseJsonQuery(String query){
        if(query==null)
            return null;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(query);
        for(String key : jsonObject.keySet()){
            if(SOSA.observes.toLowerCase().endsWith(key.toLowerCase())) {
                JsonElement value = jsonObject.get(key);
                jsonObject.remove(key);
                jsonObject.add(SOSA.observes, value);
            }

            if(SOSA.madeObservation.toLowerCase().endsWith(key.toLowerCase())) {
                JsonElement value = jsonObject.get(key);
                jsonObject.remove(key);
                jsonObject.add(SOSA.madeObservation, value);
            }
        }
        return jsonObject;
    }


}
