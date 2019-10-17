package com.agtinternational.iotcrawler.core.commands;

import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GetObservationsCommand {

    String streamId;
    int limit;

    public GetObservationsCommand(String streamId, int limit){
        this.streamId = streamId;
        this.limit = limit;

    }

    public int getLimit() {
        return limit;
    }

    public String getStreamId() {
        return streamId;
    }

    public static GetObservationsCommand fromJson(String json) throws Exception {

        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));
        JsonObject jsonObject = (JsonObject) messageObj.get("args");

        String streamId = jsonObject.get("streamId").getAsString();
        int limit = jsonObject.get("limit").getAsInt();
        GetObservationsCommand command = new GetObservationsCommand(streamId, limit);
        return command;
    }

    public String toJson() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streamId", streamId);
        jsonObject.addProperty("limit", limit);

        String command = "{ command: "+this.getClass().getSimpleName()+", args: "+jsonObject.toString()+"}";

        return command;
    }
}