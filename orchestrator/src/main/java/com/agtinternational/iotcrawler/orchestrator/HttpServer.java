package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HttpServer {
    private Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    //private String endpoint = "/notify";

    HttpClient httpClient;
    private com.sun.net.httpserver.HttpServer httpServer;
    private String url;
    private String host;
    private int port;

    public HttpServer(){

        host = (System.getenv().containsKey(Constants.HTTP_SERVER_HOST)? System.getenv(Constants.HTTP_SERVER_HOST): "localhost");
        port = (System.getenv().containsKey(Constants.HTTP_SERVER_PORT)? Integer.parseInt(System.getenv(Constants.HTTP_SERVER_PORT)): 3001);
        url = "http://"+host+":"+port;

        httpClient = HttpClients.createDefault();

    }

    public void init() throws Exception {
        LOGGER.info("Trying to init web server for listening at {}:{}", host, port);
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void addContext(String endpoint, HttpHandler httpHandler){
        LOGGER.info("Adding endpoint {}", url+endpoint);
        httpServer.createContext(endpoint, httpHandler);
    }

    public void addContext(String endpoint, Function handlingFunction){
        LOGGER.info("Adding endpoint {}", url+endpoint);
        httpServer.createContext(endpoint, new HttpHandler(){
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
                    //receivings.add(theString);
                    LOGGER.info(response);
                }
                catch (Exception e){
                    response = "Error: "+e.getLocalizedMessage();
                    LOGGER.error("Failed to read the message: {}", e.getLocalizedMessage());
                }

                if(theString!=null) {
                    try {
                        handlingFunction.apply(theString);
                    } catch (Exception e) {
                        LOGGER.error("Failed to apply handler on message: {}", e.getLocalizedMessage());
                        e.printStackTrace();
                    }
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
    }

    public HttpHandler proxyingHandler(String brokerHost) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {

                String response = "";
c
                String combinedUri = brokerHost + he.getRequestURI().toString();

                HttpRequestBase httpRequest = null;
                if (he.getRequestMethod().equals("GET")) {
                    httpRequest = new HttpGet(combinedUri);

                } else if (he.getRequestMethod().equals("POST")) {

                    httpRequest = new HttpPost(combinedUri);

                    InputStream is = he.getRequestBody();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, Charset.defaultCharset());
                    String body = writer.toString();

                    HttpEntity entity = EntityBuilder.create()
                            .setBinary(body.getBytes())
                            .build();

                    ((HttpPost) httpRequest).setEntity(entity);
                    //httpRequest.setHeader("Content-length", String.valueOf(entity.getContentLength()));
                } else if (he.getRequestMethod().equals("DELETE")) {
                    httpRequest = new HttpDelete(combinedUri);
                } else if (he.getRequestMethod().equals("PUT")) {
                    httpRequest = new HttpPut(combinedUri);
                } else if (he.getRequestMethod().equals("OPTIONS")) {
                    httpRequest = new HttpPut(combinedUri);
                } else throw new NotImplementedException(he.getRequestMethod() + " not implemented");

                LOGGER.info("{} request received to {}", he.getRequestMethod(), he.getRequestURI().toString());

                httpRequest.setHeader("Accept", "application/json");
                httpRequest.setHeader("Content-type", "application/json");

                HttpResponse response2 = httpClient.execute(httpRequest);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                response = result.toString();

//                if(theString!=null) {
//                    try {
//                        handlingFunction.apply(theString);
//                    } catch (Exception e) {
//                        LOGGER.error("Failed to apply handler on message: {}", e.getLocalizedMessage());
//                        e.printStackTrace();
//                    }
//                }

                Map<String, Object> parameters = new HashMap<String, Object>();
                for (String key : parameters.keySet())
                    response += key + " = " + parameters.get(key) + "\n";
                he.sendResponseHeaders(200, response.length());

                OutputStream os = he.getResponseBody();
                os.write(response.toString().getBytes());
                os.close();


            }
        };
    }

    public void start(){
        httpServer.start();
    }

    public String getUrl() {
        return url;
    }


    public void stop(){
        httpServer.stop(0);
    }

}
