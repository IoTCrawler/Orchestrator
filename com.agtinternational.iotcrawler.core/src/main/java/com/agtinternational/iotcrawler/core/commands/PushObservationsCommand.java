package com.agtinternational.iotcrawler.core.commands;

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

import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class PushObservationsCommand extends RPCCommand {

    List<StreamObservation> observations;

    public PushObservationsCommand(List<StreamObservation> observations){
        this.observations = observations;

    }

    public List<StreamObservation> getObservations() {
        return observations;
    }

    public static PushObservationsCommand fromJson(String json) throws Exception {
        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));

        JsonArray jsonArray = (JsonArray) messageObj.get("args");
        List<StreamObservation> observations = new ArrayList<>();
        for(Object observation: jsonArray){

            //EntityLD entityLD = EntityLD.fromJsonString(observation.toString());
            String json2 = ((JsonPrimitive) observation).getAsString();
            StreamObservation streamObservation = StreamObservation.fromJson(json2);
            observations.add(streamObservation);
        }

        PushObservationsCommand command = new PushObservationsCommand(observations);
        return command;
    }

    public String toJson() throws Exception {
        JsonArray jsonArray = new JsonArray();
        JsonParser parser = new JsonParser();
        List<String> jsons = new ArrayList<>();
        for(StreamObservation observation: observations) {
            //jsons.add(new JsonParser().parse(observation.toJsonString().replace("\n",""));
            jsonArray.add(observation.toJsonLDString());
            //jsonArray.add(parser.parse(observation.toJsonString()));
            //EntityLD entityLD = observation.toEntityLD();
            //jsonArray.add(entityLD .toJsonObject());
        }

        //String json = "{'observations': '"+jsonArray.toString()+"'}";
        //String command = "{ command: "+this.getClass().getSimpleName()+", args: ["+String.join(",", jsons.toArray(new String[0]))+"}";
        String command = "{ command: "+this.getClass().getSimpleName()+", args: "+jsonArray.toString()+"}";

        return command;
    }
}
