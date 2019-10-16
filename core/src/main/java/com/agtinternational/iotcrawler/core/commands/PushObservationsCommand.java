package com.agtinternational.iotcrawler.core.commands;

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
