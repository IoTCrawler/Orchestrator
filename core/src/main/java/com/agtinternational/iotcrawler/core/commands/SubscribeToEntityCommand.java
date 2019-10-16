package com.agtinternational.iotcrawler.core.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.lang.reflect.Type;
import java.util.List;

public class SubscribeToEntityCommand extends RPCCommand {

    String entityId;
    String[] attributes;
    List<NotifyCondition> notifyConditions;
    Restriction restriction;
    //String queueName;

    public SubscribeToEntityCommand(String entityId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction /*, String queueName*/) {
        this.entityId = entityId;
        this.attributes = attributes;
        this.notifyConditions = notifyConditions;
        this.restriction = restriction;
        //this.queueName = queueName;
    }

    public String getEntityId() {
        return entityId;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public List<NotifyCondition> getNotifyConditions() {
        return notifyConditions;
    }

    public Restriction getRestriction() {
        return restriction;
    }

//    public String getQueueName() {
//        return queueName;
//    }

    public static SubscribeToEntityCommand fromJson(String json) {

        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));
        JsonObject args = (JsonObject) messageObj.get("args");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        SubscribeToEntityCommand command = gson.fromJson(args, (Type) SubscribeToEntityCommand.class);
        return command;
    }

//    public String toJson() {
//        String json = "{'model': '"+model.toJsonString()+"'}";
//        return "{ command: "+this.getClass().getSimpleName()+", args: "+json+"}";
//    }

}