package com.agtinternational.iotcrawler.fiware.clients;/*-
 * #%L
 * fiware-clients
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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.HTTP_REFERENCE_URL;

public class HttpTestServer {
    private Logger LOGGER = LoggerFactory.getLogger(HttpTestServer.class);

    private int port = 3001;

    private com.sun.net.httpserver.HttpServer httpServer;
    private List<String> receivings = new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        HttpTestServer testServer = new HttpTestServer();
        testServer.initHttpServer();

    }

    public static String getRefenceURL(){
        return System.getenv(HTTP_REFERENCE_URL);
    }

    public void initHttpServer() throws IOException {
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/list", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                // parse request
                String response = String.join("\n", receivings);
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();

            }
        });
        httpServer.createContext("/notify", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                // parse request
                LOGGER.info("POST request received");
                String response="";
                String theString = null;
                try {
                    InputStream is = he.getRequestBody();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, Charset.defaultCharset());
                    theString = writer.toString();
                    // send response
                    response= "Received: "+theString;
                    receivings.add(theString);
                    LOGGER.info(response);
                }
                catch (Exception e){
                    response = "Error: "+e.getLocalizedMessage();
                    LOGGER.error("Failed to read the message: {}", e.getLocalizedMessage());
                }


                Map<String, Object> parameters = new HashMap<String, Object>();
                for (String key : parameters.keySet())
                    response += key + " = " + parameters.get(key) + "\n";
                he.sendResponseHeaders(200, response.length());

                OutputStream os = he.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();

            }
        });
        httpServer.start();

    }


}
