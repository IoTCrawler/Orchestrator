package com.agtinternational.iotcrawler.core.commands;

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
