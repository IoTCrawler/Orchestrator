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

import com.google.gson.*;
import com.orange.ngsi2.model.Attribute;
import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

public class EntityLD /*extends Entity*/ {
    private String id;
    private String type;
    Object context;
    Map<String, Object> attributes = new HashMap<>();

    public EntityLD() {

    }

    public EntityLD(String id, String type) {
        this.id = id;
        this.type = type;
    }


    public EntityLD(String id, String type, Map<String, Object> attributes) {
        this(id, type);
        this.attributes = attributes;
    }

//    public EntityLD(String id, String type, Map<String, Attribute> attributes) {
//        super(id, type, attributes);
//    }

    public EntityLD(String id, String type, Map<String, Object> attributes, Object context) {
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String name){
        //List<Attribute> attrList = new ArrayList<>();

        Object ret = attributes.get(name);

//        for(String key: attributes.keySet()) {
//            if(key.equals(name) || key.startsWith(name+"#")){
//                Object origAtt = attributes.get(key);
//
//                if(ret!=null){
//                   List values = null;
//                   if(!(ret.getValue() instanceof List)) {
//                       values = new ArrayList();
//                       values.add(ret.getValue());
//                   }else
//                       values = (List)ret.getValue();
//
//                   values.add(origAtt.getValue());
//                   ret.setValue(values);
//                }else {
//                    ret = new Attribute();
//                    ret.setValue(origAtt.getValue());
//                    ret.setMetadata(origAtt.getMetadata());
//                    ret.setType(origAtt.getType());
//                }
//                //Attribute attribute = attributes.get(name);
//                //attrList.add(attribute);
//            }
//        }

        return ret;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Object getContext() {
        return context;
    }

    public void addAttribute(String name, Attribute value){
        addAttributeAsObject(name, value);
    }

    public void addAttribute(String name, List<Attribute> value){
        addAttributeAsObject(name, value);
    }

    private void addAttributeAsObject(String name, Object value){
        attributes = Utils.appendAttribute(attributes, name, value);
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", getId());
        jsonObject.addProperty("type", getType());

        Map<String, Object>  attributeMap = getAttributes();
        JsonObject extraContext = new JsonObject();
        for(String key: attributeMap.keySet()) {

            Object attribute = attributeMap.get(key);
            if(attribute instanceof Iterable){
                JsonArray jsonObjectAttr = Utils.iterableToJson((Iterable)attribute);
                if(jsonObjectAttr.size()==1) {
                    jsonObject.add(key, jsonObjectAttr.get(0));
                    continue;
                }

                int index = 1;
                for (JsonElement jsonElement : jsonObjectAttr) {
//                    if(key.startsWith("http://"))
//                        key = Utils.getFragment(key);

                    String key2 = key + "#" + index;
                    jsonObject.add(key2, jsonElement);
                    String url = (key.startsWith("http://")?key:null);
                    if(url==null)
                        url = "http://dummyurl/"+key;

                    extraContext.addProperty(key2, url);
                    index++;
                }

            }else {
                JsonElement jsonObjectAttr = Utils.objectToJson(attribute);
                jsonObject.add(key, jsonObjectAttr);
            }
        }
        JsonElement contextJson = null;
        if(context!=null){
            contextJson = Utils.objectToJson(context);

            if(extraContext.size()>0) {
                if (contextJson instanceof Iterable) {
                    Iterator iterator = ((Iterable)contextJson).iterator();
                    while (iterator.hasNext()) {
                        Object item = iterator.next();
                        if(item instanceof JsonObject)
                            for (String key3 : extraContext.keySet()){
                                ((JsonObject) item).add(key3, extraContext.get(key3));
                            }
                    }
                } else if (contextJson instanceof JsonObject) {
                    for (String key3 : extraContext.keySet())
                        ((JsonObject) contextJson).add(key3, extraContext.get(key3));
                }
            }
            jsonObject.add("@context", contextJson);
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

        Map<String, Object> attributes = Utils.extractAttributes(objectMap);
        EntityLD entity = new EntityLD(nameStr, typeStr, attributes, context);
        return entity;
    }



}
