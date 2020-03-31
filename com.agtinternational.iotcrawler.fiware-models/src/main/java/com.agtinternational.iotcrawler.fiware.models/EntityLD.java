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
