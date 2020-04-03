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
