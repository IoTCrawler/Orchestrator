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
