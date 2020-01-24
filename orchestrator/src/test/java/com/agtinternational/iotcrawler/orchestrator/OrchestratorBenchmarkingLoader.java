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

    private Logger LOGGER = LoggerFactory.getLogger("OrchestratorBench");

    Thread thread;
    Orchestrator orchestrator;
    NgsiLD_MdrClient orchestratorRestClient;
    Semaphore orchestratorStartedMutex;
    String url = "http://10.67.1.107:3001/ngsi-ld/";
    //String url = "http://localhost:3001/ngsi-ld/";

    @Before
    public void init() {
        super.init();
    }

    @Test
    @Ignore
    public void benchmarkingOperationsTest() throws Exception {


        int num_of_threads = 512;
        int tasks_per_thread = 10;
        int experiments = 10;

        final Map<String, Long> vars = new HashMap<>();
        List<Callable<String>> tasks = new ArrayList<>();

        int totalTasks = 0;
        for(int i=0; i<num_of_threads;i++){
            final NgsiLD_MdrClient orchestratorRestClient = new NgsiLD_MdrClient(url);

            for(int j=0; j<tasks_per_thread; j++) {
                final int k = totalTasks;
                tasks.add(new Callable<String>() {
                    @Override
                    public String call() throws Exception {


                        IoTStream ioTStream1 = new IoTStream("Stream_"+String.valueOf(k)+"_"+System.currentTimeMillis());
                        long started = System.currentTimeMillis();
                        List<EntityLD>  result = orchestratorRestClient.getEntities(IoTStream.getTypeUri(),null,null, 0,1);
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

        LOGGER.info("NumThreads: {}; Tasks per thread: {} Tasks total: {}", num_of_threads, tasks_per_thread, totalTasks);

        long totalLat = 0;
        long totalThroughtput = 0;
        for(int i=0; i<experiments; i++){
            ExecutorService es = Executors.newFixedThreadPool(num_of_threads);
            long started = System.currentTimeMillis();
            es.invokeAll(tasks);
            long runtime = System.currentTimeMillis() - started;

            long waitingTime = vars.values().stream().mapToLong(e -> e).sum();
            long avgLatency = waitingTime / vars.size();
            double throughtput = Math.round(totalTasks/(runtime/1000.0));

            LOGGER.info("Exp {} done. {} metrics in {} ms. Waiting time: {}; Throughtput: {} tasks/s; Avg latency is {}", i, vars.size(), runtime, waitingTime, throughtput, avgLatency);

            if(i>1) {//skipping first 2 runs because of rest
                totalLat += avgLatency;
                totalThroughtput+=throughtput;
            }

            vars.clear();
            //Thread.sleep(3000);
        }

        LOGGER.info("{} Experiments done. Throughtput: {} tasks/s; Latency: {} ms", experiments-2, totalThroughtput/(experiments-2), totalLat/(experiments-2));
    }
}
