package com.agtinternational.iotcrawler.fiware.clients;


import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orange.ngsi2.model.Attribute;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.jackson.map.ObjectMapper;
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

public class NgsiLDConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    //private String jsonPrefix;
    //protected ObjectMapper objectMapper;

//    public NgsiLDConverter() {
//       // this(Jackson2ObjectMapperBuilder.json().build());
//    }

    //@Override
    //public boolean  canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    public boolean  canRead(Class<?> clazz, MediaType mediaType){
        return true;// this.canRead(type.getClass(), mediaType);
    }

    //@Override
    //public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
    public boolean canWrite(Class<?> clazz, MediaType mediaType){
        return true; // this.canWrite(type.getClass(), mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;// Entity.class == clazz;
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputStream = inputMessage.getBody();

//        StringWriter writer = new StringWriter();
//        IOUtils.copy(inputStream, writer, Charset.defaultCharset());
//        String theString = writer.toString();

        List<EntityLD> ret = new ArrayList<>();
        Object parsedObject = JsonUtils.fromInputStream(inputStream);
        if(parsedObject instanceof Map) {
            Map objectMap = (Map)parsedObject;
            try {
                EntityLD entity = EntityLD.fromMapObject(objectMap);
                ret.add(entity);
            }
            catch (Exception e){
                LOGGER.error("Failed to convert object to EntityLD: {}", ((JsonElement)parsedObject).toString());
                e.printStackTrace();
            }
        }else if(parsedObject instanceof List){
            for(Object object: (List)parsedObject)
            try{
                EntityLD entity = EntityLD.fromMapObject((Map)object);
                ret.add(entity);
            }
            catch (Exception e){
                LOGGER.error("Failed to convert object to EntityLD: {}", ((JsonElement)object).toString());
                e.printStackTrace();
            }
        }else
            throw new NotImplementedException("Not implemented");

        return ret.toArray(new EntityLD[0]);
    }



    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        JsonObject jsonObject = new JsonObject();
        if(object instanceof EntityLD){
            EntityLD entity = (EntityLD)object;

            jsonObject = entity.toJsonObject();
            if(!jsonObject.has("id")){
                jsonObject.add("id", jsonObject.get("@id"));
                jsonObject.remove("@id");
            }

            if(!jsonObject.has("type")) {
                jsonObject.add("type", jsonObject.get("@type"));
                jsonObject.remove("@type");
            }
        }else if(object instanceof Map){
            jsonObject = Utils.mapToJson((Map)object);
        }

        String str = Utils.prettyPrint(jsonObject);

        StreamUtils.copy((byte[])str.getBytes(), outputMessage.getBody());
    }


    @Override
    public boolean canRead(Type type, Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.read(contextClass,inputMessage);
    }



    //@Override
    public void write(Object o, Type type, MediaType mediaType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.write(o, mediaType, outputMessage);
    }




}