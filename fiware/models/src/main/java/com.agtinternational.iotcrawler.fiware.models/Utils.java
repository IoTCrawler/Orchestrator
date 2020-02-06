package com.agtinternational.iotcrawler.fiware.models;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.fiware.models.NGSILD.DateTime;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.GeoProperty;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.*;
import com.orange.ngsi2.model.Attribute;
import com.orange.ngsi2.model.Metadata;
import org.apache.commons.lang3.NotImplementedException;

import java.net.URI;
import java.util.*;


public class Utils {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String prettyPrint(JsonElement jsonElement){
        return gson.toJson(jsonElement);
    }

//    public static JsonObject mapToJson(Map mapObject){
//        JsonObject ret = new JsonObject();
//        for(Object key : mapObject.keySet()){
//            Object value = mapObject.get(key);
//
//            if(value instanceof String)
//                ret.addProperty(key.toString(), (String)value);
//            else if(value instanceof Number)
//                ret.addProperty(key.toString(), (Number)value);
//            else {
//                JsonElement valueJson = objectToJson(value);
//                ret.add(key.toString(), valueJson);
//            }
//
//        }
//        return ret;
//    }

//    public static JsonElement objectToJson(Object value){
//        if(value instanceof Map) {
//
//            return mapToJson((Map) value);
//        }else if(value instanceof Iterable) {
//            Iterator iterator = ((Iterable)value).iterator();
//            JsonArray jsonArray = new JsonArray();
//            while(iterator.hasNext()) {
//                Object value = iterator.next()
//                jsonArray.add(objectToJson());
//            }
//            return jsonArray;
//        }else if(value instanceof Attribute)
//            return attributeToJson((Attribute)value);
//        else
//            throw new NotImplementedException(value.getClass().getName());
//    }

    public static JsonElement objectToJson(Object object){

        if(object.getClass().isArray())
            object = Arrays.asList((Object[])object);

        if(object instanceof Property)
            return Utils.attributeToJson((Attribute) object);

        if(object instanceof String)
            return new JsonPrimitive((String)object);

        if(object instanceof Number)
            return new JsonPrimitive((Number) object);

        if(object instanceof Boolean)
            return  new JsonPrimitive((Boolean) object);

        if(object instanceof Map){
            JsonObject jsonObject = new JsonObject();
            Map mapObject = (Map)object;
            for(Object key : mapObject.keySet()){
                Object value = mapObject.get(key);
                JsonElement jsonElement = objectToJson(value);
                jsonObject.add(key.toString(), jsonElement);
            }
            return jsonObject;
        }

        if(object instanceof Iterable){
            Iterator iterator = ((Iterable)object).iterator();
            JsonArray jsonArray = new JsonArray();
            while(iterator.hasNext()){
                Object value = iterator.next();
                JsonElement jsonElement = objectToJson(value);
                jsonArray.add(jsonElement);
            }
            return jsonArray;
        }

        throw new NotImplementedException("");

    }

    public static JsonObject attributeToJson(Attribute attribute){
        JsonObject ret = new JsonObject();
        String type = attribute.getType().get().toString();
        Object value0 = attribute.getValue();
        ret.addProperty("type", type);

        JsonElement jsonElement = objectToJson(value0);

        String attKey = (type.toLowerCase().equals("relationship")? "object": "value");
        ret.add(attKey, (JsonElement) jsonElement);

        if(attribute instanceof Relationship){
            Map<String, Object> attributes = ((Relationship)attribute).getAttributes();
            for(String key: attributes.keySet()){
                Object value = attributes.get(key);
                if(value instanceof String)
                    ret.addProperty(key, String.valueOf(value));
                else if(value instanceof Metadata){
                    if(((Metadata) value).getType()!=null)
                        ret.add(key, attributeToJson((Attribute) value));
                    else
                        ret.addProperty(key, ((Attribute) value).getValue().toString());
                }else if(value instanceof Attribute) {
                    if (((Attribute) value).getType() != null)
                        ret.add(key, attributeToJson((Attribute) value));
                    else
                        ret.addProperty(key, ((Attribute) value).getValue().toString());
                }else{
                    throw new NotImplementedException(value.getClass().getName());
                }
            }
        }

        if(attribute.getMetadata()!=null)
            for(String key: attribute.getMetadata().keySet()){
                Object value = attribute.getMetadata().get(key);

                if(value instanceof String)
                    ret.addProperty(key, String.valueOf(value));
                else if(value instanceof Metadata){
                    if(((Metadata) value).getType()!=null)
                        ret.add(key, metadataToJson((Metadata) value));
                    else
                        ret.addProperty(key, ((Metadata) value).getValue().toString());
                }else{
                    throw new NotImplementedException(value.getClass().getName());
                }

            }
        return ret;
    }

    public static JsonObject metadataToJson(Metadata metadata){
        JsonObject ret = new JsonObject();
        ret.addProperty("type", metadata.getType());
        Object value = metadata.getValue();
        if(value instanceof String) {
            if(metadata.getType().toLowerCase().equals("relationship"))
                ret.addProperty("object", String.valueOf(metadata.getValue()));
            else
                ret.addProperty("value", String.valueOf(metadata.getValue()));
            //ret.addProperty("value", String.valueOf(metadata.getObject()));
        }else{
            throw new NotImplementedException("");
        }
        return ret;
    }

    public static Map<String, Object> extractAllProperties(Map<String, Object> attMap) throws Exception {
        Map<String, Object> ret = new HashMap<>();
        //for(Map<String, Object> attMap : attrs)
        Attribute attribute = null;
        for(String attKey : attMap.keySet()){
            Object attValue = attMap.get(attKey);

            //System.out.println(attKey+" => "+attValue);

            List<Object> values = new ArrayList<>();
            List<Object> attributes = new ArrayList<>();

            if(attValue instanceof Map) {
                Attribute attribute2 = extractAttribute((Map)attValue);
                ret.put(attKey, attribute2);
            }else if(attValue instanceof Iterable){
                Iterator iterator = ((Iterable)attValue).iterator();
                while (iterator.hasNext()) {
                    Object value = iterator.next();
                    if(value instanceof Map) {
                        Attribute attribute2 = extractAttribute((Map) value);
                        attributes.add(attribute2);
                    }else if(value instanceof String){
                        values.add(value);
                    }else
                        throw new org.apache.commons.lang3.NotImplementedException(value.getClass().getName()+" not implemented ");
                }
                if(values.size()>0){
                    attributes.addAll(values);
                    //attributes.add(new Property(values));
                }
                ret.put(attKey, attributes);
            }else if(attValue instanceof String || attValue instanceof Number){
                //attributes.add(new Property(attValue));
                ret.put(attKey, attValue);
            }else
                throw new org.apache.commons.lang3.NotImplementedException(attValue.getClass().getName()+" not implemented ");



        }
        return ret;
    }

    public static Attribute extractAttribute(Map<String, Object> attMap) throws Exception {
//        Attribute ret = null;
//        Map<String, Object> attributes = new LinkedHashMap<>();

        Object value = null;
        if(attMap.containsKey("type"))
            value = attMap.get("type");

        if(attMap.containsKey("@type"))
            value = attMap.get("@type");

        if(value!=null)
            if (value.toString().toLowerCase().equals("property") || value.toString().equals(Property.getTypeUri())) {
                return new Property(attMap);
            } else if (value.toString().toLowerCase().equals("relationship") || value.toString().equals(Relationship.getTypeUri()))
                return new Relationship(attMap);
            else if (value.toString().toLowerCase().equals("geoproperty") || value.toString().equals(GeoProperty.getTypeUri()))
                return new GeoProperty(attMap);
            else if (value.toString().toLowerCase().equals("datetime") || value.toString().equals(DateTime.getTypeUri()))
                return new DateTime(attMap);
            else
                throw new org.apache.commons.lang3.NotImplementedException(value.getClass().getName()+" not implemented ");


//        for(String attKey0 : attMap.keySet()) {
//            Object attValue = attMap.get(attKey0);
//            String attKey = attKey0.replace("@","");
//
//            if(attValue instanceof Map) {
//                Attribute attribute2 = extractAttribute((Map)attValue);
//                attributes.put(attKey, attribute2);
//                //throw new NotImplementedException();
//            }else if(attValue instanceof Number) {
//                ret = new Property();
//                ret.setValue(attValue);
//            }else if(attValue instanceof String) {
//                //attribute = new Attribute();
////                    if (attKey.equals("object")) {
////                        ((Relationship) ret).setObject(attValue);
////                        //attributes.put(attKey, attValue);
////                    }else
//                if (attKey.equals("object") || attKey.equals("value") || attKey.equals(NGDSI_LD_NS+"hasValue"))
//                    ret.setValue(attValue);
//                else if(attValue instanceof String) {
//                    attributes.put(attKey, attValue);
//                }else
//                    throw new org.apache.commons.lang3.NotImplementedException(attValue.getClass().getName()+" not implemented ");
////                    Attribute attribute2 = new Attribute();
////                    attribute2.setValue(attValue);
////                    attributes.put(attKey, attribute2);
//
//            }else if(attValue instanceof Iterable) {
//                Iterator iterator = ((Iterable)attValue).iterator();
//                while (iterator.hasNext()) {
//                    Object value2 = iterator.next();
//                    String abc= "123";
//                }
//            } else{
//                throw new org.apache.commons.lang3.NotImplementedException(attValue.getClass().getName()+" not implemented ");
//            }
//        }
//        if(ret instanceof Relationship)
//            ((Relationship)ret).setAttributes(attributes);
//        if(ret==null)
//            throw new Exception("No attribute was extracted for ");
//        return ret;
        return null;
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

}
