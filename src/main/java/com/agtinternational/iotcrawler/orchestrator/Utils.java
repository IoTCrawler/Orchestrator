package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Utils {


//    public static JsonObject correctQuery(JsonObject query){
//        if(query!=null) {
//            for (String key : query.keySet()) {
//                Object value = query.get(key);
//                if (value instanceof JsonPrimitive)
//                    value = ((JsonPrimitive) value).getAsString();
//                else if (value instanceof JsonArray) {
//                    List<String> values = new ArrayList<>();
//                    for (JsonElement element : ((JsonArray) value)) {
//                        values.add(element.getAsString());
//                    }
//                    value = String.join(",", values);
//                } else
//                    throw new NotImplementedException();
//                pairs.add(key + ".object=" + value.toString());
//            }
//        }
//
//    }


}
