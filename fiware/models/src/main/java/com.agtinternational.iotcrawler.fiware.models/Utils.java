package com.agtinternational.iotcrawler.fiware.models;

import com.agtinternational.iotcrawler.fiware.models.NGSILD.DateTime;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.GeoProperty;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
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

    public static JsonObject mapToJson(Map mapObject){
        JsonObject ret = new JsonObject();
        for(Object key : mapObject.keySet()){
            Object value = mapObject.get(key);

            if(value instanceof String)
                ret.addProperty(key.toString(), (String)value);
            else if(value instanceof Number)
                ret.addProperty(key.toString(), (Number)value);
            else {
                JsonElement valueJson = objectToJson(value);
                ret.add(key.toString(), valueJson);
            }
//            if(value instanceof Map)
//                ret.add(key.toString(),  mapToJson((Map)value));
//            else if(value instanceof Iterable) {
//                Iterator iterator = ((Iterable)value).iterator();
//                JsonArray jsonArray = new JsonArray();
//                while(iterator.hasNext())
//                    jsonArray.add(objectToJson(iterator.next()));
//                ret.add(key.toString(), jsonArray);
//            }else if(value instanceof Attribute)
//                ret.add(key.toString(),  attributeToJson((Attribute)value));
//            else if(value instanceof String)
//                ret.addProperty(key.toString(), (String)value);
//            else if(value instanceof Number)
//                ret.addProperty(key.toString(), (Number)value);
//            else
//                throw new NotImplementedException(value.getClass().getName());
        }
        return ret;
    }

    public static JsonElement objectToJson(Object value){
        if(value instanceof Map)
            return mapToJson((Map)value);
        else if(value instanceof Iterable) {
            Iterator iterator = ((Iterable)value).iterator();
            JsonArray jsonArray = new JsonArray();
            while(iterator.hasNext())
                jsonArray.add(objectToJson(iterator.next()));
            return jsonArray;
        }else if(value instanceof Attribute)
            return attributeToJson((Attribute)value);
        else
            throw new NotImplementedException(value.getClass().getName());
    }

    public static JsonObject attributeToJson(Attribute attribute){
        JsonObject ret = new JsonObject();
        ret.addProperty("type", attribute.getType().get());
        if(attribute.getType().get().toLowerCase().equals("relationship"))
            ret.addProperty("object", String.valueOf(attribute.getValue()));
        else
            ret.addProperty("value", String.valueOf(attribute.getValue()));

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
            List<Attribute> attributes = new ArrayList<Attribute>();

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
                    attributes.add(new Property(values));
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
