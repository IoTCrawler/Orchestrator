//package com.agtinternational.iotcrawler.orchestrator;
//
//import com.agtinternational.iotcrawler.core.interfaces.Component;
//import org.hobbit.sdk.docker.AbstractDockerizer;
//import org.hobbit.sdk.docker.builders.BuildBasedDockersBuilder;
//import org.hobbit.sdk.utils.CommandQueueListener;
//import org.junit.Test;
//
//
//public class OrchestatorDockerizedTest {
//
//    Component orchestator;
//    private AbstractDockerizer rabbitMqDockerizer;
//    private BuildBasedDockersBuilder orchestatorDockerizerBuilder;
//
//    public void init(Boolean useCachedImage) throws Exception {
//
//        orchestatorDockerizerBuilder = new BuildBasedDockersBuilder("iotc-dockerizer")
//                                    .dockerfilePath("Dockerfile_notUsed")
//                                    .imageName("iotcrawler/orchestrator")
//                                    .useCachedImage(useCachedImage);
//
//    }
//
//    @Test
//    public void buildImages() throws Exception {
//
//        init(false);
//
//        orchestatorDockerizerBuilder.build().prepareImage("iotcrawler/orchestrator");
////        MultiThreadedImageBuilder builder = new MultiThreadedImageBuilder(8);
////        builder.addTask(orchestatorDockerizerBuilder);
////        builder.build();
//
//    }
//
//    @Test
//    public void checkHealth() throws Exception {
//
//        orchestator = new Orchestrator();
//
////        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
////        componentsExecutor.submit(orchestatorDockerizerBuilder.build(), "iotc-dockerizer");
////        orchestatorDockerizerBuilder.build().startContainer();
//    }
//
//    @Test
//    public void checkHealthDockerized() throws Exception {
//
//        init(true);
////        Component orchestator = orchestatorDockerizerBuilder.build();
////
//        CommandQueueListener commandQueueListener = new CommandQueueListener();
//
//        orchestatorDockerizerBuilder.build().run();
//
//        //ComponentsExecutor componentsExecutor = new ComponentsExecutor();
//        //componentsExecutor.submit(orchestatorDockerizerBuilder.build(), "iotc-dockerizer");
//
//        //commandQueueListener.waitForTermination();
//        //commandQueueListener.terminate();
//        //componentsExecutor.shutdown();
//
//
//
//    }
//
//}
