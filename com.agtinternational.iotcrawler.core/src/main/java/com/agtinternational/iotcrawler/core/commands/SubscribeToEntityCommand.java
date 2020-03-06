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
