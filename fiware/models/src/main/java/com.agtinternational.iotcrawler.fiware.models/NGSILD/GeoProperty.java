package com.agtinternational.iotcrawler.fiware.models.NGSILD;

import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.orange.ngsi2.model.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.agtinternational.iotcrawler.fiware.models.Constants.NGDSI_LD_NS;

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
