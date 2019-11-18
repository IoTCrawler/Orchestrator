package com.agtinternational.iotcrawler.core.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class GetEntitiesCommand extends RPCCommand {

    int offset = 0;
    int limit = 0;
    String[] ids;
    //String query;
    JsonObject jsonQuery;
    String typeURI;

    public GetEntitiesCommand(String[] ids, int offset, int limit){
        this.ids = ids;
        this.offset = offset;
        this.limit = limit;
    }

    public GetEntitiesCommand(String[] ids, String typeURI, int offset, int limit){
        this(ids, offset, limit);
        this.typeURI = typeURI;
    }

    public GetEntitiesCommand(String typeURI, JsonObject query, int offset, int limit){
        this.typeURI = typeURI;
        this.jsonQuery = query;
        this.offset = offset;
        this.limit = limit;
    }

    public GetEntitiesCommand(String typeURI, int offset, int limit){
        this.typeURI = typeURI;
        this.limit = limit;
        this.offset = offset;
        this.limit = limit;
    }

    public String[] getIds() {
        return ids;
    }

    public String getTypeURI() {
        return typeURI;
    }

    public int getOffset() { return offset; }

    public int getLimit() {
        return limit;
    }

    public JsonObject getJsonQuery() {
        return jsonQuery;
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
