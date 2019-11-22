package com.agtinternational.iotcrawler.core.interfaces;

/*-
 * #%L
 * core
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

import com.agtinternational.iotcrawler.core.OrchestratorRPCClient;
import com.agtinternational.iotcrawler.core.models.*;

import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

//    @Override
//    public List<IoTStream> getStreams(int offset, int limit) throws Exception {
//        return iotCrawlerClient.getStreams(offset, limit);
//    }

    @Override
    public List<IoTStream> getStreams(String query, Map<String, Number> ranking, int offset, int limit) throws Exception {
        return iotCrawlerClient.getStreams(query, ranking, offset, limit);
    }

//    @Override
//    public List<Sensor> getSensors(int offset, int limit) throws Exception {
//        return iotCrawlerClient.getSensors(offset, limit);
//    }

    @Override
    public List<Sensor> getSensors(String query, int offset, int limit) throws Exception {
        return iotCrawlerClient.getSensors(query, offset, limit);
    }

//    @Override
//    public List<SosaPlatform> getPlatforms(int offset, int limit) throws Exception {
//        return iotCrawlerClient.getPlatforms(offset, limit);
//    }

    @Override
    public List<Platform> getPlatforms(String query, int offset, int limit) throws Exception {
        return iotCrawlerClient.getPlatforms(query,offset, limit);
    }

//    @Override
//    public List<ObservableProperty> getObservableProperties(int offset, int limit) throws Exception {
//        return iotCrawlerClient.getObservableProperties(offset, limit);
//    }

    @Override
    public List<ObservableProperty> getObservableProperties(String query, int offset, int limit) throws Exception {
        return iotCrawlerClient.getObservableProperties(query, offset, limit);
    }


//    @Override
//    public List<EntityLD> getEntities(String entityType, int limit) throws Exception {
//        return iotCrawlerClient.getEntities(entityType, limit);
//    }

    @Override
    public List<String> getEntityURIs(String query, int offset, int limit) throws Exception {
        return iotCrawlerClient.getEntityURIs(query, offset, limit);
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
