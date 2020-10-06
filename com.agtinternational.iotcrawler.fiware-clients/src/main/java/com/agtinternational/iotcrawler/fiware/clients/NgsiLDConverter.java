package com.agtinternational.iotcrawler.fiware.clients;

/*-
 * #%L
 * fiware-clients
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


import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.*;
import com.orange.ngsi2.model.Attribute;
import com.orange.ngsi2.model.Entity;
import com.orange.ngsi2.model.Subscription;
import eu.neclab.iotplatform.ngsi.api.datamodel.StatusCode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

//import com.orange.ngsi2.model.Entity;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.management.InstanceNotFoundException;

public class NgsiLDConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    //private String jsonPrefix;
    //protected ObjectMapper objectMapper;

    public NgsiLDConverter() {
       // this(Jackson2ObjectMapperBuilder.json().build());
        this.setSupportedMediaTypes(Arrays.asList(new MediaType[]{ MediaType.parseMediaType("application/ld+json") }));
    }

    //@Override
    //public boolean  canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    public boolean  canRead(Class<?> clazz, MediaType mediaType){
        boolean ret = clazz.getCanonicalName().contains(EntityLD.class.getCanonicalName());
        //boolean ret = (clazz ==EntityLD.class);
        return ret;// this.canRead(type.getClass(), mediaType);
    }

    //@Override
    //public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
    public boolean canWrite(Class<?> clazz, MediaType mediaType){
        if(clazz==HashMap.class || EntityLD.class == clazz)
        //if(clazz.getCanonicalName().contains(EntityLD.class.getCanonicalName()) || clazz.getCanonicalName().contains(HashMap.class.getCanonicalName()))
            return true; // this.canWrite(type.getClass(), mediaType);
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> aClass, MediaType mediaType) {
        return canWrite(aClass,mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        if(EntityLD.class == clazz)
            return true;
        return false;
    }

    @Override
    public boolean canRead(Type type, Class<?> aClass, MediaType mediaType) {
        //if(clazz.getCanonicalName().contains(EntityLD.class.getCanonicalName()) || clazz.getCanonicalName().contains(HashMap.class.getCanonicalName()))
        boolean ret = this.supports(((Class)type).getComponentType()) && this.canRead(mediaType);
        return ret;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.read(contextClass,inputMessage);
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = inputMessage.getBody();

//        StringWriter writer = new StringWriter();
//        IOUtils.copy(inputStream, writer, Charset.defaultCharset());
//        String theString = writer.toString();

        List<EntityLD> ret = new ArrayList<>();
        Object parsedObject = null;
        try {
            parsedObject = JsonUtils.fromInputStream(inputStream);
        }
        catch (Exception e){
            LOGGER.error("Failed to parse response into a json: {}", e.getLocalizedMessage());
            return null;
        }
        if(parsedObject instanceof Map){
            Map objectMap = (Map)parsedObject;

            if(objectMap.containsKey("error")) {
                throw new IOException("Json-response contains an error: "+ objectMap.get("error"));
            }

            if(objectMap.get("type").toString().endsWith("ResourceNotFound")){
                throw new IOException(new HttpClientErrorException(HttpStatus.valueOf(404)));
                //return ret;
            }
            try {
                EntityLD entity = EntityLD.fromMapObject(objectMap);
                ret.add(entity);
            }
            catch (Exception e){
                throw new IOException("Failed to convert object to EntityLD: "+((JsonElement)parsedObject).toString());
                //LOGGER.error("Failed to convert object to EntityLD: {}", ((JsonElement)parsedObject).toString());
               // e.printStackTrace();
            }
        }else if(parsedObject instanceof List){
            for(Object object: (List)parsedObject)
                if(!((Map)object).isEmpty())
                    try{
                        EntityLD entity = EntityLD.fromMapObject((Map)object);
                        ret.add(entity);
                    }
                    catch (Exception e){
                        throw new IOException("Failed to convert object to EntityLD: "+((JsonElement)parsedObject).toString());
                        //LOGGER.error("Failed to convert object to EntityLD: {}", ((JsonElement)object).toString());
                       // e.printStackTrace();
                    }
        }else
            throw new NotImplementedException("Not implemented");
        if(aClass==EntityLD.class)
            return ret.get(0);
        return ret.toArray(new EntityLD[0]);
    }


    //@Override
    public void write(Object o, Type type, MediaType mediaType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.write(o, mediaType, outputMessage);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        JsonObject jsonObject = new JsonObject();
        if(object instanceof EntityLD){
            EntityLD entity = (EntityLD)object;

            try {
                jsonObject = entity.toJsonObject();
            }
            catch (Exception e){
                throw new IOException("Failed to convert EntityLD to JSON object", e.getCause());
            }
            if(!jsonObject.has("id")){
                jsonObject.add("id", jsonObject.get("@id"));
                jsonObject.remove("@id");
            }

            if(!jsonObject.has("type")) {
                jsonObject.add("type", jsonObject.get("@type"));
                jsonObject.remove("@type");
            }

//            if(jsonObject.has("@context")) {
//                Object context = jsonObject.get("@context");
//                if (!(context instanceof JsonArray)){
//                    JsonArray jsonArray = new JsonArray();
//                    jsonArray.add(NGSILD.CORE_CONTEXT);
//                    jsonArray.add( (JsonObject)context );
//                    jsonObject.add("@context", jsonArray);
//                }else if(((JsonArray)context).contains(new JsonPrimitive(NGSILD.CORE_CONTEXT))){
//                    ((JsonArray)context).add(new JsonPrimitive(NGSILD.CORE_CONTEXT));
//                    jsonObject.add("@context", (JsonArray)context);
//                }
//
//                //jsonObject.add("@context", context);
//                //jsonObject.add("context", jsonObject.get("@context"));
//                //jsonObject.remove("@context");
//            }
//        }else if(object instanceof Subscription){


        }else if(object instanceof Map){
            jsonObject = (JsonObject) Utils.objectToJson(object);
        }else{
            throw new NotImplementedException("Conversion not implemented for "+object.getClass());
        }

        String str = Utils.prettyPrint(jsonObject);

        LOGGER.debug(str);

        StreamUtils.copy((byte[])str.getBytes(), outputMessage.getBody());
    }


}
