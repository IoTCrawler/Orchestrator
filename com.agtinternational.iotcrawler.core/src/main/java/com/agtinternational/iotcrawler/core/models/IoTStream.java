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
        return iotcNS+"IotStream";
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

    public Object generatedBy(){
        return getAttribute(iotcNS+"generatedBy");
    }



}
