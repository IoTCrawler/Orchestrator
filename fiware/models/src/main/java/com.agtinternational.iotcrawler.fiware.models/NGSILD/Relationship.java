package com.agtinternational.iotcrawler.fiware.models.NGSILD;

import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.orange.ngsi2.model.Attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.agtinternational.iotcrawler.fiware.models.Constants.NGDSI_LD_NS;

public class Relationship extends Property {

//    protected Map<String, Object> attributes = new HashMap<>();

    public Relationship() {
        setType(Optional.of(getTypeUri()));
    }

    public Relationship(Map<String, Object> attMap) throws Exception {
        super(attMap);
        if (attributes.containsKey("object")) {
            setValue(attributes.get("object"));
            attributes.remove("object");
        }
    }

//    public Relationship(Property property){
//        this();
//        setType(property.getType());
//        setValue(property.getValue());
//        setMetadata(property.getMetadata());
//    }

//    public void setAttributes(Map<String, Object> attributes) {
//        this.attributes.putAll(attributes);
//    }
//
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }

    public Relationship(Object value) {
        this();
        setObject(value);
    }

    public void setObject(Object value) {
        setValue(value);
    }

    public Object getObject() {
        return getValue();
    }



    public static String getTypeUri(){
        return "Relationship";//djane broker accepts only this
        //return NGDSI_LD_NS+"Relationship";
    }

}
