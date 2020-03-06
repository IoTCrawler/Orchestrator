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

import java.lang.reflect.Type;
import java.util.Map;

public class GetEntitiesCommand extends RPCCommand {

    int offset = 0;
    int limit = 0;
    String[] ids;
    String targetClass;
    String query;
    String typeURI;
    Map<String, Number> ranking;

    public GetEntitiesCommand(String[] ids, int offset, int limit){
        this.ids = ids;
        this.offset = offset;
        this.limit = limit;
    }

    public GetEntitiesCommand(String[] ids, String typeURI, int offset, int limit){
        this(ids, offset, limit);
        this.typeURI = typeURI;
    }

    public GetEntitiesCommand(String typeURI, int offset, int limit){
        this.typeURI = typeURI;
        this.limit = limit;
        this.offset = offset;
        this.limit = limit;
    }

    public GetEntitiesCommand(String typeURI, String query, Map<String, Number> ranking, int offset, int limit){
        this.typeURI = typeURI;
        this.query = query;
        this.ranking = ranking;
        this.offset = offset;
        this.limit = limit;
    }

    public GetEntitiesCommand(Class targetClass, String query, Map<String, Number> ranking, int offset, int limit){
        this.targetClass = targetClass.getName();
        this.query = query;
        this.ranking = ranking;
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

    public String getQuery() {
        return query;
    }

    public Map<String, Number> getRanking() {
        return ranking;
    }

    public String getTargetClass() {
        return targetClass;
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
