package com.agtinternational.iotcrawler.core.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class GetEntitiesCommand extends RPCCommand {

    int limit = 0;
    String[] ids;
    String query;
    String typeURI;

    public GetEntitiesCommand(String[] ids){
        this.ids = ids;
    }

    public GetEntitiesCommand(String[] ids, String typeURI){
        this(ids);
        this.typeURI = typeURI;
    }

    public GetEntitiesCommand(String typeURI, String query){
        this.typeURI = typeURI;
        this.query = query;
    }

    public GetEntitiesCommand(String typeURI, int limit){
        this.typeURI = typeURI;
        this.limit = limit;
    }

    public String[] getIds() {
        return ids;
    }

    public String getQuery() {
        return query;
    }

    public String getTypeURI() {
        return typeURI;
    }

    public int getLimit() {
        return limit;
    }

    public static GetEntitiesCommand fromJson(String json) {

        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));
        JsonObject args = (JsonObject) messageObj.get("args");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        GetEntitiesCommand command = gson.fromJson(args.toString(), (Type) GetEntitiesCommand.class);
        return command;
    }
}
