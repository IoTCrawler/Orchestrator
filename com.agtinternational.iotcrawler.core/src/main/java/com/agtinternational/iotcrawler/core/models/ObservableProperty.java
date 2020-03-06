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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.sosaNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;


public class ObservableProperty extends RDFModel {

    public ObservableProperty(String uri){
        super(uri, getTypeUri());
    }

    public ObservableProperty(String uri, String label){
        this(uri);
        addProperty(RDFS.label, label);
    }

    public ObservableProperty(String uri, Model model){
        this(uri);
        this.model = model;
    }

//    public ObservableProperty(String uri, String classURI, String label){
//        this(uri, classURI);
//        addProperty(RDFS.label, label);
//    }

    public void isObservedBy(Object value){
        addProperty(SOSA.isObservedBy, value);
    }

    public Object isObservedBy(){
        Object ret = getAttribute(SOSA.isObservedBy);
        return ret;
    }

    public static String getTypeUri() {
        return SOSA.observableProperty;
    }

    public static ObservableProperty fromEntity(EntityLD entity) throws Exception {
        entity.setType(getTypeUri());
        RDFModel rdfModel = RDFModel.fromEntity(entity);
        return new ObservableProperty(rdfModel.getURI(), rdfModel.getModel());
    }

}
