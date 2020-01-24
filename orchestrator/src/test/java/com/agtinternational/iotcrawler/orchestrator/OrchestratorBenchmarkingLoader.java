package com.agtinternational.iotcrawler.orchestrator;

import com.agtinternational.iotcrawler.core.models.IoTStream;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.orchestrator.clients.NgsiLD_MdrClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

public class OrchestratorBenchmarkingLoader extends EnvVariablesSetter{

    private Logger LOGGER = LoggerFactory.getLogger(OrchestratorTest.class);

    Thread thread;
    Orchestrator orchestrator;
    NgsiLD_MdrClient orchestratorRestClient;
    Semaphore orchestratorStartedMutex;
    //String url = "http://10.67.1.107:3001/ngsi-ld/";
    String url = "http://localhost:3001/ngsi-ld/";

    @Before
    public void init() {
        super.init();
    }

    @Test
    @Ignore
    public void benchmarkingOperationsTest() throws Exception {



        int num_of_threads = 64;
        ExecutorService es = Executors.newFixedThreadPool(num_of_threads);
        final Map<String, Long> vars = new HashMap<>();
        List<Callable<String>> tasks = new ArrayList<>();


        int tasks_per_thread = 5;
        int totalTasks = 0;
        for(int i=0; i<num_of_threads;i++){
            final NgsiLD_MdrClient orchestratorRestClient = new NgsiLD_MdrClient( url);

            for(int j=0; j<tasks_per_thread; j++) {
                final int k = totalTasks;
                tasks.add(new Callable<String>() {
                    @Override
                    public String call() throws Exception {

                        long started = System.currentTimeMillis();
                        IoTStream ioTStream1 = new IoTStream("Stream_"+String.valueOf(k)+"_"+System.currentTimeMillis());
                        List<EntityLD>  result = orchestratorRestClient.getEntities(IoTStream.getTypeUri(),null,null, 0,0);
                        //Boolean result = orchestratorRestClient.registerEntity(ioTStream1);
                        //if (result) {
                        long took = System.currentTimeMillis() - started;
                        vars.put(String.valueOf(k), took);
                        //}
                        return null;
                    }
                });
                totalTasks++;
            }
        }
        es.invokeAll(tasks);

        long totalTime = vars.values().stream().mapToLong(e->e).sum();
        Number avg = totalTime/vars.size();
        LOGGER.info("Avg time is {}",avg);

    }
}
