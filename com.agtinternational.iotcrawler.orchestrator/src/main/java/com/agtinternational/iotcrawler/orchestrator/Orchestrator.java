package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
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

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.core.clients.RabbitClient;
import com.agtinternational.iotcrawler.core.interfaces.Component;
import com.agtinternational.iotcrawler.core.interfaces.IoTCrawlerClient;
import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.subscription.Subscription;
//import com.agtinternational.iotcrawler.orchestrator.clients.IotBrokerDataClient;
import com.agtinternational.iotcrawler.orchestrator.clients.AbstractMetadataClient;
import com.agtinternational.iotcrawler.orchestrator.clients.NgsiLD_MdrClient;
import com.agtinternational.iotcrawler.core.models.*;
import com.google.gson.*;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.rabbitmq.client.*;

import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyContextRequest;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
//import eu.neclab.iotplatform.ngsi.api.datamodel.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.core.Constants.*;
import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;
import static com.agtinternational.iotcrawler.orchestrator.Constants.RANKING_COMPONENT_URL;


public class Orchestrator implements Component {

    private Logger LOGGER = LoggerFactory.getLogger(Orchestrator.class);

    String rabbitHost = "localhost";
    String redisHost = "localhost";
    String notificationsEndpoint = "/notify";
    String ngsiEndpoint = "/ngsi-ld";
    String versionEndpoint = "/version";
    boolean cutURIs = true;

    NgsiLD_MdrClient metadataClient;
    HttpServer httpServer;

    Semaphore httpServiceFinishedMutex = new Semaphore(0);
    JsonParser jsonParser = new JsonParser();


    Map<String, String> rpcSubscriptionRequests = new HashMap<>();
    Map<String, String> declaredExchanges = new HashMap<>();
    //Map<String, Function<String, Void>> rpcSubscriptions = new HashMap<>();
    //List<String> rpcSubscriptions = new ArrayList<>();
    Map<Pair<String, String[]>, Function<StreamObservation, Void>> knownSubscriptions = new HashMap<>();

    RabbitClient rabbitClient;
    RedisClient redisClient;
    StatefulRedisConnection<String, String> redisConnection;
    RedisCommands<String, String> redisSyncCommands;

    private boolean serverStarted;
    private Connection connection;


    public Orchestrator(Boolean cutURIs) {
        this.cutURIs = cutURIs;
    }

    public static void main(String[] args) throws Exception {
        Boolean cutURIs = (System.getenv().containsKey(CUT_TYPE_URIS)?Boolean.parseBoolean(System.getenv(CUT_TYPE_URIS)):false);
        Orchestrator orchestrator = new Orchestrator(cutURIs);
        orchestrator.init();
        orchestrator.run();
    }

    public void init() throws Exception {

        //metadataClient = new TripleStoreMDRClient();
        if(!System.getenv().containsKey(HTTP_REFERENCE_URL))
            LOGGER.error("No HTTP_REFERENCE_URL for setting up for an endpoint");

        if(!System.getenv().containsKey(RANKING_COMPONENT_URL))
            LOGGER.warn("RANKING_COMPONENT_URL is not specified");

        String brokerURL = (System.getenv().containsKey(RANKING_COMPONENT_URL)? System.getenv(RANKING_COMPONENT_URL): System.getenv(NGSILD_BROKER_URL));

        // ngsiLDClient = new NgsiLDClient(brokerURL);//
        metadataClient = new NgsiLD_MdrClient(brokerURL, this.cutURIs);
        LOGGER.info("Initialized NGSI-LD Client to {}", brokerURL);

//        dataBrokerClient = new IotBrokerDataClient();
//        LOGGER.info("Initialized IoTBroker Client to {}", dataBrokerClient.getIoTBrokerEndpoint());

        if (System.getenv().containsKey(IOTCRAWLER_RABBIT_HOST))
            rabbitHost = System.getenv(IOTCRAWLER_RABBIT_HOST);

        if (System.getenv().containsKey(IOTCRAWLER_REDIS_HOST))
            redisHost = System.getenv(IOTCRAWLER_REDIS_HOST);

        rabbitClient = new RabbitClient(rabbitHost);

    }

    public void run() throws Exception {
        LOGGER.info("Starting orchestrator");

        LOGGER.info("Syncing with Redis at {}", "redis://"+redisHost);
        redisClient = RedisClient.create("redis://"+redisHost);
        //redisConnection = redisClient.connect();
        //redisSyncCommands = redisConnection.sync();

        LOGGER.info("Initializing web server");
        httpServer = new HttpServer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rabbitClient.init();
                } catch (Exception e) {
                    LOGGER.error("Failed to init rabbit client");
                    //e.printStackTrace();
                }
            }
        }).start();

        initHttpServer();
        startHttpServer();
    }

    private void initHttpServer(){
        LOGGER.info("Initializing web server");

        try {
            httpServer.init();
        }
        catch (Exception e){
            LOGGER.error("Failed to init http server: {}", e.getLocalizedMessage());
            e.printStackTrace();
            return;
        }

        httpServer.addContext(notificationsEndpoint, notificationsHandlerFunction);
        httpServer.addContext(ngsiEndpoint, httpServer.proxyingHandler(metadataClient.ngsiLDClient, metadataClient.getBrokerHost(), new Function<String, String>() {
            @Override
            public String apply(String subscriptionId) {
                try {
                    rabbitClient.declareExchange(subscriptionId);
                    //String queueName = rabbitClient.declareBoundQueue(subscriptionId);
                    return subscriptionId;
                } catch (Exception e) {
                    LOGGER.error("Failed to declare exchange or bound a queue: {}", e.getCause().getLocalizedMessage());
                    e.printStackTrace();
                }
                return null;
            }
        }));
        httpServer.addContext(versionEndpoint,  httpServer.versionsHandler());
    }



    private Function<String, Void> notificationsHandlerFunction = new Function<String, Void>() {
        @Override
        public Void apply(String body) {
            LOGGER.debug("notificationsHandler triggered");
            if(body.length()==0)
                return null;


            NotifyContextRequest notification = (NotifyContextRequest)NotifyContextRequest.parseStringToJson(body, NotifyContextRequest.class);
            String subscriptionId = notification.getSubscriptionId();

            if(!declaredExchanges.containsKey(subscriptionId)) {
                try {
                    rabbitClient.declareExchange(subscriptionId);
                    declaredExchanges.put(subscriptionId, null);
                }catch (Exception e){
                    LOGGER.error("Failed to declare exchange {}", subscriptionId);
                    return null;
                }
            }
            LOGGER.debug("Publishing to Rabbit");
            rabbitClient.publish(subscriptionId, body);
            return null;
        }
    };



    private void startHttpServer(){
        if(httpServer==null){
            LOGGER.info("Starting http server skipped");
            return;
        }
        LOGGER.info("Starting built-in web server");
        try {
            httpServer.start();
            serverStarted = true;
            httpServiceFinishedMutex.acquire();
            LOGGER.info(" [*] Waiting for RPC messages via REST");
        } catch (Exception e) {
            LOGGER.error("Failed to start web server: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }


    }







    @Override
    public void close() throws IOException {
        httpServer.stop();
    }
}
