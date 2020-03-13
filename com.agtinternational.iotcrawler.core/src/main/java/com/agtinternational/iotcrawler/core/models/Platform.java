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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class Platform extends RDFModel {


    public Platform(String uri){
        super(uri, getTypeUri());
    }

    public Platform(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

//    public Platform(String uri, String classURI, String label){
//        this(uri, label);
//        setType(classURI);
//    }

    public Platform(String uri, Model model){
        super(uri, getTypeUri(), model);
        //setType(Platform.getTypeUri());
    }

    public void hosts(Object value){
        addProperty(SOSA.hosts, value);
    }

    public Object hosts(){
        Object ret = getAttribute(SOSA.hosts);
        return ret;
    }

    public String location(){
        String ret = (String)getAttribute(iotcNS + "Location");
        return ret;
    }

    public void location(Object value){
        addProperty(iotcNS + "Location", value);
    }

    public static String getTypeUri(){
        //return SosaPlatform.class.getSimpleName();
        return SOSA.platform;
    }

    public static Platform fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new Platform(rdfModel.getURI(), rdfModel.getModel());
    }

    public static Platform fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static Platform fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new Platform(rdfModel.getURI(), rdfModel.getModel());
    }

//    public static SosaPlatform fromJsonObject(JsonObject jsonObject) {
//        String uri = agtSmartHomeNS +"#Device_"+jsonObject.get("Hostname").hashCode();
//        SosaPlatform ret = new SosaPlatform(uri, jsonObject.get("Hostname").getAsString());
//        return ret;
//    }
}
