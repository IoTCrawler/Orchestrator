package com.agtinternational.iotcrawler.orchestrator;

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
