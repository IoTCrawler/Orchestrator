package com.agtinternational.iotcrawler.fiware.models.NGSILD;

import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.orange.ngsi2.model.Attribute;

import java.util.Map;
import java.util.Optional;

import static com.agtinternational.iotcrawler.fiware.models.Constants.NGDSI_LD_NS;

public class DateTime extends Property {

    public DateTime() {
        setType(Optional.of(getTypeUri()));
    }

//    public DateTime(Property property){
//        this();
//        setType(property.getType());
//        setValue(property.getValue());
//        setMetadata(property.getMetadata());
//    }

    public DateTime(Object value){
        this();
        setValue(value);
    }

    public static String getTypeUri(){
        return "DateTime";//djane broker accepts only this
        //return NGDSI_LD_NS+"DateTime";
    }


    public DateTime(Map<String, Object> attMap) throws Exception {
        super(attMap);
//        Property property = Property.fromMap(attMap);
//        DateTime ret = new DateTime(property);
//        return ret;
    }
}
