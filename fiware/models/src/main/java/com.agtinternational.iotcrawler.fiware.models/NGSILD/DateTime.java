package com.agtinternational.iotcrawler.fiware.models.NGSILD;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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
