package com.agtinternational.iotcrawler.orchestrator;

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
