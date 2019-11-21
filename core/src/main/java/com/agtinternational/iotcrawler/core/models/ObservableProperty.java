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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

import static com.agtinternational.iotcrawler.core.Constants.sosaNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;


public class ObservableProperty extends RDFModel {

    public ObservableProperty(String uri){
        super(uri, getTypeUri());
    }

    public ObservableProperty(String uri, String classURI){
        super(uri, classURI);
    }

    public ObservableProperty(String uri, Model model){
        super(uri, model);
    }

    public ObservableProperty(String uri, String classURI, String label){
        this(uri, classURI);
        addProperty(RDFS.label, label);
    }

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
