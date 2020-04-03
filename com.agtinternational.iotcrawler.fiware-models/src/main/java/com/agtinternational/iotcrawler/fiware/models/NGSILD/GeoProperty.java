package com.agtinternational.iotcrawler.fiware.models.NGSILD;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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


import java.util.Map;
import java.util.Optional;


public class GeoProperty extends Property {

    public GeoProperty() {
        setType(Optional.of(getTypeUri()));
    }

//    public GeoProperty(Property property) {
//        this();
//        setType(property.getType());
//        setValue(property.getValue());
//        setMetadata(property.getMetadata());
//    }

    public GeoProperty(Map<String, Object> attMap) throws Exception {
        super(attMap);
        //String abc
//        Property property = Property.fromMap(attMap);
//        GeoProperty ret = new GeoProperty(property);
//        return ret;
    }

    public void setObject(Object value) {
        setValue(value);
    }

    public Object getObject() {
        return getValue();
    }


    public void setAttributes(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public static String getTypeUri(){
        return "GeoProperty";//djane broker accepts only this
        //return NGDSI_LD_NS+"GeoProperty";
    }

}
