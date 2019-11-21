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

import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GetObservationsCommand {

    String streamId;
    int offset = 0;
    int limit = 0;

    public GetObservationsCommand(String streamId, int offset, int limit){
        this.streamId = streamId;
        this.offset = offset;
        this.limit = limit;

    }

    public int getOffset() {
        return offset;
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
        int offset = jsonObject.get("offset").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();
        GetObservationsCommand command = new GetObservationsCommand(streamId, offset, limit);
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
