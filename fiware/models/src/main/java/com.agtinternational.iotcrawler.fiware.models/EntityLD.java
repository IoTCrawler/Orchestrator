package com.agtinternational.iotcrawler.fiware.models;

import com.agtinternational.iotcrawler.fiware.models.NGSILD.Property;
import com.google.gson.*;
import com.orange.ngsi2.model.Attribute;
import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

public class EntityLD /*extends Entity*/ {
    private String id;
    private String type;
    Object context;
    Map<String, Attribute> attributes;

    public EntityLD() {
    }

    public EntityLD(String id, String type) {
        this.id = id;
        this.type = type;
    }


    public EntityLD(String id, String type, Map<String, Attribute> attributes) {
        this(id, type);
        this.attributes = attributes;
    }

//    public EntityLD(String id, String type, Map<String, Attribute> attributes) {
//        super(id, type, attributes);
//    }

    public EntityLD(String id, String type, Map<String, Attribute> attributes, Object context) {
        this(id, type, attributes);
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Object getContext() {
        return context;
    }

    public void addAttribute(String name, Property value){
        attributes.put(name, value);
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("type", getType());
        if(context!=null){
            JsonElement contextJson = Utils.objectToJson(context);
            jsonObject.add("@context", contextJson);
        }

        Map<String, Attribute>  attributeMap = getAttributes();
        for(String key: attributeMap.keySet()) {

            Object attribute = attributeMap.get(key);
            JsonElement jsonObjectAttr=null;
            if(attribute instanceof Attribute) {
                jsonObjectAttr = Utils.attributeToJson((Attribute) attribute);
            }else if(attribute instanceof Iterable){
                jsonObjectAttr = new JsonArray();
                Iterator iterator = ((Iterable) attribute).iterator();
                while (iterator.hasNext()){
                    JsonObject jsonObject1 = Utils.attributeToJson((Attribute)iterator.next());
                    ((JsonArray) jsonObjectAttr).add(jsonObject1);
                }
            }else
                 throw new NotImplementedException(attribute.getClass().getName()+" not implemented ");
            //attributeToJson()
            //JsonObject attributeJson = new JsonObject();
            //attributeJson.addProperty("type", attribute.getType().get());
            //attributeJson.addProperty("object", attribute.getObject());
            jsonObject.add(key, jsonObjectAttr);
        }
        return jsonObject;
    }

    private static JsonArray listToJson(List listObject){
        JsonArray contextJson = new JsonArray();
        for(Object item : listObject){
            if(item instanceof String)
                ((JsonArray) contextJson).add((String)item);
            else if(item instanceof Number)
                ((JsonArray) contextJson).add((Number) item);
            else
                throw new NotImplementedException(item.getClass().getName()+" not implemented ");
        }
        return contextJson;
    }



    public static EntityLD fromJsonString(String jsonString) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Object map = mapper.readValue(jsonString, HashMap.class);

        EntityLD ret = EntityLD.fromMapObject((Map)map);
        return ret;
    }

    public static EntityLD fromMapObject(Map objectMap) throws Exception {
        String nameStr = objectMap.get((objectMap.containsKey("@id")?"@id":"id")).toString();
        String typeStr = objectMap.get((objectMap.containsKey("@type")?"@type":"type")).toString();
        Object contextObj = objectMap.get((objectMap.containsKey("@context")?"@context":"context"));


        Object context = null;
        if(contextObj!=null) {
            if (contextObj instanceof JsonObject)
                contextObj = new Gson().fromJson(contextObj.toString(), HashMap.class);

            if (contextObj instanceof String)
                context = (String) contextObj;
            else if (contextObj instanceof Map)
                context = (Map<String, Object>) contextObj;
            else if (contextObj instanceof List){
                context = (List)contextObj;
            }else
                throw new NotImplementedException(contextObj.getClass().getName()+" not implemented ");
        }

        objectMap.remove((objectMap.containsKey("@id")?"@id":"id"));
        objectMap.remove((objectMap.containsKey("@type")?"@type":"type"));
        objectMap.remove((objectMap.containsKey("@context")?"@context":"context"));

        Map<String, Attribute> attributes = Utils.extractAllProperties(objectMap);
        EntityLD entity = new EntityLD(nameStr, typeStr, attributes, context);
        return entity;
    }



}
