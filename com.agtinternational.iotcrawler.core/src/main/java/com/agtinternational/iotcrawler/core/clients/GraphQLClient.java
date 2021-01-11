package com.agtinternational.iotcrawler.core.clients;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_GRAPHQL_ENDPOINT;

public class GraphQLClient {

    private Logger LOGGER = LoggerFactory.getLogger(GraphQLClient.class);

    String endpoint;
    CloseableHttpClient httpclient;
    public GraphQLClient(String endpoint){
        this.endpoint = endpoint;
        httpclient = HttpClients.createDefault();
    }

    public JsonElement query(String query) throws Exception {

        JsonElement ret = null;

        HttpPost httppost = new HttpPost(endpoint);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json");
        JsonObject queryObject = new JsonObject();
        queryObject.addProperty("query", query);
        httppost.setEntity(new StringEntity(queryObject.toString(), ContentType.APPLICATION_JSON));
        LOGGER.debug("Performing query to {}", endpoint);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                Scanner s = new Scanner(instream).useDelimiter("\\A");
                String json = (s.hasNext()?s.next():"");

                JsonParser jsonParser = new JsonParser();

                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                JsonElement result = jsonParser.parse(reader);
                if(((JsonObject)result).has("error")) {
                    String error = ((JsonObject) result).get("error")+": "+ ((JsonObject) result).get("message");
                    throw new Exception(error);
                }

                ret = ((JsonObject)result).get("data");

            } catch (Exception e) {
                //String test = "";
                e.printStackTrace();
            } finally {
                instream.close();
            }
        }
        return ret;
    }

    public String getStreamObservationsByStreamId(String streamId) throws Exception {
        String query = "{  streamObservations(belongsTo: {    id: \""+streamId+"\"  }){  id  }}";
        JsonElement result = query(query);
        String ret = null;
        if(((JsonObject)result).has("streamObservations")){
            JsonArray streamObservations = (JsonArray)((JsonObject)result).get("streamObservations");
            if(streamObservations.size()>0)
                ret = ((JsonObject)streamObservations.get(0)).get("id").getAsString();
        }
        return ret;
    }
}
