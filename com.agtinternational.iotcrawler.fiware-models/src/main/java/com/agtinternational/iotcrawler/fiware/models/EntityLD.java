package com.agtinternational.iotcrawler.fiware.models;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

    public Attribute getAttribute(String name){
        Attribute ret = attributes.get(name);
        return ret;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Object getContext() {
        return context;
    }

    public void addAttribute(String name, Attribute value){
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
            JsonElement jsonObjectAttr = Utils.objectToJson(attribute);
            jsonObject.add(key, jsonObjectAttr);
        }
        return jsonObject;
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
