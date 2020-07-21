package com.agtinternational.iotcrawler.core;

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

import com.agtinternational.iotcrawler.core.clients.GraphQLClient;
import com.agtinternational.iotcrawler.core.models.IoTStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Scanner;

import static com.agtinternational.iotcrawler.core.Constants.IOTCRAWLER_GRAPHQL_ENDPOINT;


public class GraphqlTests {

    GraphQLClient graphQLClient;

    @Before
    public void init(){
        EnvVariablesSetter.init();
        String endpoint = System.getenv().get(IOTCRAWLER_GRAPHQL_ENDPOINT);
        graphQLClient = new GraphQLClient(endpoint);
    }

    @Test
    public void testGraphQLQuery() throws Exception {

        String query = "{\n" +
                "  #streamObservations{\n" +
                "  streamObservations(belongsTo: {\n" +
                "    id: \"urn:household1:stateStream\"\n" +
                "  }){\n" +
                "    id,\n" +
                "    hasSimpleResult,\n" +
                "    belongsTo{\n" +
                "      id\n" +
                "    }\n" +
                "  }\n" +
                "  \n" +
                "}";
        JsonElement data = graphQLClient.query(query);

        JsonElement jsonElement = ((JsonObject)data).get("streams");
        String abc = "";
    }

    @Test
    public void getStreamObservationsByStreamIdTest() throws Exception {


        String streamID = graphQLClient.getStreamObservationsByStreamId("urn:household1:stateStream");


        String abc = "";
    }

//    @Test
//    public void testAppoloClient() throws Exception {
//        String endpoint = System.getenv().get(IOTCRAWLER_GRAPHQL_ENDPOINT);
//        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
//        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
//                .url(endpoint)
//                //.variables(new Variable<>("timeFormat", "MM/dd/yyyy"))
//                .arguments(
//                        new Arguments("stream.id", new Argument<>("id", "d070633a9f9"))
//                )
//                //.scalars(BigDecimal.class)
//                .request(IoTStream.class)
//                .build();
//        String ret = requestEntity.toString();
//        //GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder().build();
//
//        GraphQLResponseEntity<IoTStream> responseEntity = graphQLTemplate.query(requestEntity, IoTStream.class);
//        String test = "123";
//    }
}
