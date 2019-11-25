package com.agtinternational.iotcrawler.core.models;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.sosaNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;

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

    public void observes(Object value){
        addProperty(SOSA.observes, value);
    }

    public String observes(){
        String ret = (String)getAttribute(SOSA.observes);
        return ret;
    }

    public void isHostedBy(Object value){
        addProperty(SOSA.isHostedBy, value);
    }

    public String isHostedBy(){
        String ret = (String)getAttribute(SOSA.isHostedBy);
        return ret;
    }

    public static String getTypeUri(){
        return sosaPrefix+":"+"Sensor";
        //return Sensor.class.getSimpleName();
        //return "sosa:Sensor";
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
