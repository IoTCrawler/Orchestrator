package com.agtinternational.iotcrawler.core.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class GetEntityCommand extends RPCCommand {

    int limit = 0;
    String query;
    String entityType;

    public GetEntityCommand(String entityType, String query){
        this.entityType = entityType;
        this.query = query;
    }

    public GetEntityCommand(String entityType, int limit){
        this.entityType = entityType;
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public String getEntityType() {
        return entityType;
    }

    public int getLimit() {
        return limit;
    }

    public static GetEntityCommand fromJson(String json) {

        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));
        JsonObject args = (JsonObject) messageObj.get("args");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        GetEntityCommand command = gson.fromJson(args.toString(), (Type) GetEntityCommand.class);
        return command;
    }
}
