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
