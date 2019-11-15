package com.agtinternational.iotcrawler.core.interfaces;

import com.agtinternational.iotcrawler.core.OrchestratorRPCClient;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;

import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class IoTCrawlerApp extends IotCrawlerClient implements Component {

    protected IotCrawlerClient iotCrawlerClient;

    public IoTCrawlerApp(){
        iotCrawlerClient = new OrchestratorRPCClient();
    }

    public void init() throws Exception{
        iotCrawlerClient.init();
    }

    public void run() throws Exception{
        iotCrawlerClient.run();
    }


    @Override
    public Boolean registerEntity(RDFModel entity) throws Exception {
        return iotCrawlerClient.registerEntity(entity);
    }

    @Override
    public List<IoTStream> getStreams(int offset, int limit) throws Exception {
        return iotCrawlerClient.getStreams(offset, limit);
    }

    @Override
    public List<IoTStream> getStreams(String query,int offset, int limit) throws Exception {
        return iotCrawlerClient.getStreams(query, offset, limit);
    }

    @Override
    public List<Sensor> getSensors(int offset, int limit) throws Exception {
        return iotCrawlerClient.getSensors(offset, limit);
    }

    @Override
    public List<Sensor> getSensors(String query,int offset, int limit) throws Exception {
        return iotCrawlerClient.getSensors(query, offset, limit);
    }

    @Override
    public List<SosaPlatform> getPlatforms(int offset, int limit) throws Exception {
        return iotCrawlerClient.getPlatforms(offset, limit);
    }

    @Override
    public List<SosaPlatform> getPlatforms(String query,int offset, int limit) throws Exception {
        return iotCrawlerClient.getPlatforms(query,offset, limit);
    }

    @Override
    public List<ObservableProperty> getObservableProperties(int offset, int limit) throws Exception {
        return iotCrawlerClient.getObservableProperties(offset, limit);
    }

    @Override
    public List<ObservableProperty> getObservableProperties(String query,int offset, int limit) throws Exception {
        return iotCrawlerClient.getObservableProperties(query, offset, limit);
    }


//    @Override
//    public List<EntityLD> getEntities(String entityType, int limit) throws Exception {
//        return iotCrawlerClient.getEntities(entityType, limit);
//    }

    @Override
    public List<String> getEntityURIs(String query) throws Exception {
        return iotCrawlerClient.getEntityURIs(query);
    }

//    public List<StreamObservation> getObservations() throws Exception {
//        return iotCrawlerClient.getObservations();
//    }


    public List<StreamObservation> getObservations(String streamId, int offset, int limit) throws Exception {
        return iotCrawlerClient.getObservations(streamId, offset, limit);
    }

    @Override
    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception{
        return iotCrawlerClient.pushObservationsToBroker(observations);
    }

    public void pushObservation(StreamObservation observation) throws Exception{
        iotCrawlerClient.pushObservationsToBroker(new ArrayList<StreamObservation>(){{ add(observation); }});
    }

    //@Override
//    public void subscribeTo(IoTStream ioTStream, String[] attributes, String referenceUrl) {
//
//    }

    public String subscribeTo(String streamURI, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception {
        return iotCrawlerClient.subscribeTo(streamURI, attributes, notifyConditions, restriction, onChange);
    }

    public void close() throws IOException {

    }
}
