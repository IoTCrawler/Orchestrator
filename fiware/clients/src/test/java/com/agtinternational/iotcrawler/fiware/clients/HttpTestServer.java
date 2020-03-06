/*-
 * #%L
 * fiware-clients
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
