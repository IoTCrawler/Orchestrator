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

//import com.agtinternational.iotcrawler.core.LinkFilter;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

import java.net.URI;
import java.util.List;

import static com.agtinternational.iotcrawler.core.Constants.*;

public class IoTStream extends RDFModel {

    //public static String madeBySensor = sosaNS+"madeBySensor";

    public IoTStream(String uri){

        super(uri, getTypeUri());
    }

    public IoTStream(String uri, String label){

        this(uri);
        addProperty(RDFS.label, label);

    }


    public IoTStream(String uri, Model model){
        super(uri, getTypeUri(), model);
    }

    public static String getTypeUri(){
        return iotcNS+"IoTStream";
        //return iotcPrefix+":"+"iot-stream";//IoTStream.class.getSimpleName();
    }

    public static IoTStream fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new IoTStream(rdfModel.getURI(), rdfModel.getModel());
    }


    public static IoTStream fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static IoTStream fromJson(String jsonString){
        RDFModel rdfModel = RDFModel.fromJson(jsonString);
        return new IoTStream(rdfModel.getURI(), rdfModel.getModel());
    }

//    public ObservableProperty getObservableProperty(){
//        return new ObservableProperty("http://Uri");
//    }

    public Object madeBySensor(){
        return getAttribute(SOSA.madeBySensor);
    }



}
