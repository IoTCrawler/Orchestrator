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


    @Before
    public void init() {
        super.init();
        //using NgsiLD_MdrClient for testing orchestrator's NGSILD REST interface
        //orchestratorRestClient = new NgsiLD_MdrClient( "http://localhost:3001/ngsi-ld/");
        orchestratorStartedMutex = new Semaphore(0);


//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    orchestrator.run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//
//        try {
//            LOGGER.info("Waiting 3 seconds to start orchestrator");
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        LOGGER.info("Starting tests");
    }

    @Test
    @Ignore
    public void benchmarkingOperationsTest() throws Exception {



        int num_of_threads = 1;
        ExecutorService es = Executors.newFixedThreadPool(num_of_threads);
        final Map<String, Long> vars = new HashMap<>();
        List<Callable<String>> tasks = new ArrayList<>();


        int tasks_per_thread = 1;
        int totalTasks = 0;
        for(int i=0; i<num_of_threads;i++){
            final NgsiLD_MdrClient orchestratorRestClient = new NgsiLD_MdrClient( "http://10.67.1.107:3001/ngsi-ld/");

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
