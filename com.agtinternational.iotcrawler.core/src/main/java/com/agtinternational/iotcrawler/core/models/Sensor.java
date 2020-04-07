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

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.ontologies.Geo;
import com.agtinternational.iotcrawler.core.ontologies.SOSA;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;


public class Sensor extends RDFModel {



    public Sensor(String uri){
        super(uri, getTypeUri());
    }

    public Sensor(String uri, Model model){
        super(uri, getTypeUri(), model);
    }


    public Sensor(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

//    public void madeObservation(Object value){
//        addProperty(SOSA.madeObservation, value);
//    }
//
//    public Object madeObservation(){
//        Object ret = getAttribute(SOSA.madeObservation);
//        return ret;
//    }

    public void observes(Object value){
        addProperty(SOSA.observes, value);
    }

    public Object observes(){
        Object ret = getAttribute(SOSA.observes);
        return ret;
    }

    public String location(){
        String ret = (String)getAttribute(Geo.Point);
        return ret;
    }

    public void location(Object value){
        addProperty(Geo.Point, value);
    }

    public void isHostedBy(Object value){
        addProperty(SOSA.isHostedBy, value);
    }

    public Object isHostedBy(){
        Object ret = getAttribute(SOSA.isHostedBy);
        return ret;
    }

    public static String getTypeUri(){
        return SOSA.sensor;
        //return sosaPrefix+":"+"Sensor";
        //return Sensor.class.getSimpleName();
        //return "sosa:Sensor";
    }

    public static String getTypeUri(Boolean cutURI){
        if(cutURI)
            return Utils.cutURL(getTypeUri(), namespaces);
        return getTypeUri();
    }

    public static Sensor fromEntity(EntityLD entity) throws Exception {
        //entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new Sensor(rdfModel.getURI(), rdfModel.getModel());
    }

    public static Sensor fromJsonObject(String jsonString) {
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new Sensor(rdfModel.getURI(), rdfModel.getModel());
    }



}
