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

import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class HttpServer {
    private Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    //private String endpoint = "/notify";

    private com.sun.net.httpserver.HttpServer httpServer;
    private String url;
    private String host;
    private int port;

    public HttpServer(){

        host = (System.getenv().containsKey(Constants.HTTP_SERVER_HOST)? System.getenv(Constants.HTTP_SERVER_HOST): "localhost");
        port = (System.getenv().containsKey(Constants.HTTP_SERVER_PORT)? Integer.parseInt(System.getenv(Constants.HTTP_SERVER_PORT)): 3003);
        url = "http://"+host+":"+port;

    }

    public void init() throws Exception {
        LOGGER.info("Trying to init web server for listening at {}:{}", host, port);
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void addContext(String endpoint, HttpHandler handler){
        LOGGER.info("Adding endpoint {}", url+endpoint);
        httpServer.createContext(endpoint, handler);
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
