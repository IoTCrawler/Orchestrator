package com.agtinternational.iotcrawler.core.models;

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

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.agtinternational.iotcrawler.core.Constants.iotcNS;
import static com.agtinternational.iotcrawler.core.Constants.iotcPrefix;


public class StreamObservation extends RDFModel {
    private static Logger LOGGER = LoggerFactory.getLogger(StreamObservation.class);

//    public StreamObservation(String uri, HashMap attributes){
//        super(URI.create(uri), URI.create(Constants.observationURI));
//    }

    public StreamObservation(String uri) {
        super(uri,  getTypeUri());
    }

    public StreamObservation(String uri, Model model){
        super(uri, getTypeUri(), model);
    }

    public static String getTypeUri(){
        //return StreamObservation.class.getSimpleName();
        //return "iotc:StreamObservation";
        return iotcNS+":"+"StreamObservation";
    }

    public void madeBySensor(Object value){
        addProperty(SOSA.madeBySensor, value);
    }

    public Object madeBySensor(){
        Object ret = getAttribute(SOSA.madeBySensor);
        return ret;
    }

    public static StreamObservation fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static StreamObservation fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromContextElement(ContextElement contextElement){
        RDFModel rdfModel = RDFModel.fromContextElement(contextElement);
        return new StreamObservation(rdfModel.getURI(), rdfModel.getModel());
    }

    public static StreamObservation fromJsonObject(String uri, String basicNS, JsonObject jsonObject) {
        StreamObservation ret = new StreamObservation(uri);
        for(String key: jsonObject.keySet()) {
            try {
                ret.addProperty(basicNS + key, jsonObject.get(key));
            }
            catch (Exception e){
                e.printStackTrace();
                LOGGER.error("Failed to add property: {}", key);
            }
        }
        return ret;
    }

//    public String getBelongsToStreamUri(){
//        Property property = model.createProperty(belongsTo);
//        if(!model.listObjectsOfProperty(property).hasNext())
//            return null;
//        String ret = model.listObjectsOfProperty(property).next().asResource().getURI();
//        return ret;
//    }



}
