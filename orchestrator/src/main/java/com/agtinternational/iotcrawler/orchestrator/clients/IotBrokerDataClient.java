package com.agtinternational.iotcrawler.orchestrator.clients;

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

import com.agtinternational.iotcrawler.fiware.clients.CustomSubscribeContextRequest;
import com.agtinternational.iotcrawler.fiware.clients.PatchedSouthBound;
import com.agtinternational.iotcrawler.orchestrator.Constants;
import com.agtinternational.iotcrawler.core.models.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.apache.jena.rdf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IotBrokerDataClient /*extends TripleStoreClient*/ implements AbstractDataClient {
    private static Logger LOGGER = LoggerFactory.getLogger(IotBrokerDataClient.class);
    private String ioTBrokerEndpoint;

    PatchedSouthBound iotBrokerClient;

    public IotBrokerDataClient(){
        this((System.getenv().containsKey(Constants.IOT_BROKER_URL)?System.getenv(Constants.IOT_BROKER_URL):"http://localhost:8060/ngsi10"));
    }

    public IotBrokerDataClient(String ioTBrokerEndpoint){
        //super(defaultTripleStoreURI);
        this.ioTBrokerEndpoint = ioTBrokerEndpoint;

        LOGGER.info("Initializing IoTBrokerClient to {}", ioTBrokerEndpoint);

        iotBrokerClient = new PatchedSouthBound();
        iotBrokerClient.setNgsi9url(ioTBrokerEndpoint+"/");
        iotBrokerClient.setNgsi9RemoteUrl(ioTBrokerEndpoint+"/");
        iotBrokerClient.setNgsi9rootPath("ngsi9");
        iotBrokerClient.setDefaultContentType("application/json");

    }

    public static ContextElement createContextElement(StreamObservation observation){
        Model observationModel = observation.getModel();

        List<ContextAttribute> contextAttributeList = new ArrayList<ContextAttribute>();
        StmtIterator iterator = observationModel.listStatements();
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            if(statement.getObject().isLiteral()) {
                Literal literal = statement.getLiteral();
                //List<ContextMetadata> metadata = new ArrayList<ContextMetadata>();//{{ add(new ContextMetadata("units", URI.create("units"), "dB")); }};
                List<ContextMetadata> metadata = null;
                //ContextAttribute contextAttribute = new ContextAttribute(pair.getKey(), URI.create("float"), String.valueOf(System.nanoTime()), metadata);
                //ContextAttribute contextAttribute = new ContextAttribute(iotcNS +"#"+statement.getPredicate().getLocalName(), URI.create(literal.getDatatypeURI()), literal.getString(), metadata);
                ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getURI(), URI.create(literal.getDatatypeURI()), literal.getString(), metadata);
                contextAttributeList.add(contextAttribute);
            //}else if(statement.getPredicate().hasURI(belongsTo)){
                //ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getURI(), URI.create(iotStreamURI), statement.getObject().asResource().getURI(), null);
                //contextAttributeList.add(contextAttribute);
            }else{
                LOGGER.info("Skipped as non literal: {}", statement.getPredicate().getURI());
                String abc="123";
            }
        }

        //String id = observationURI+"_made_by_"+ URI.create(observation.getBelongsToStreamUri()).getFragment();
        String id = observation.getURI();
        ContextElement ret = new ContextElement();
        ret.setEntityId(new EntityId(id, URI.create(StreamObservation.getTypeUri()), false));
        ret.setContextAttributeList(contextAttributeList);
        ret.setDomainMetadata(new ArrayList<ContextMetadata>());
        return ret;
    }

//    public static ContextElement createContextElement(IoTStream ioTStream){
//        Model iotStreamModel = ioTStream.getModel();
//
//        List<ContextAttribute> contextAttributeList = new ArrayList<ContextAttribute>();
//        StmtIterator iterator = iotStreamModel.listStatements();
//        while (iterator.hasNext()) {
//            Statement statement = iterator.nextStatement();
//            if(statement.getObject().isLiteral()) {
//                Literal literal = statement.getLiteral();
//                //List<ContextMetadata> metadata = new ArrayList<ContextMetadata>();//{{ add(new ContextMetadata("units", URI.create("units"), "dB")); }};
//                List<ContextMetadata> metadata = null;
//                //ContextAttribute contextAttribute = new ContextAttribute(pair.getKey(), URI.create("float"), String.valueOf(System.nanoTime()), metadata);
//                ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getLocalName(), URI.create(literal.getDatatypeURI()), literal.getString(), metadata);
//                contextAttributeList.add(contextAttribute);
//            }else if(statement.getPredicate().hasURI(Constants.belongsTo)){
//                ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getLocalName(), URI.create(Constants.iotStreamURI), statement.getObject().asResource().getURI(), null);
//                contextAttributeList.add(contextAttribute);
//            }else{
//                LOGGER.info("Skipped as non literal: {}", statement.getPredicate().getURI());
//                String abc="123";
//            }
//        }
//
//        //String id = observationURI+"_made_by_"+ URI.create(observation.getBelongsToStreamUri()).getFragment();
//        ContextElement ret = new ContextElement();
//        //ret.setEntityId(new EntityId(id, URI.create(observationURI), false));
//        ret.setContextAttributeList(contextAttributeList);
//        ret.setDomainMetadata(new ArrayList<ContextMetadata>());
//        return ret;
//    }

    @Override
    public Boolean pushObservations(List<StreamObservation> observations) {
        List<ContextElement> contextElements = new ArrayList<>();
        for(StreamObservation observation : observations){
            //String belongsTo = observation.getBelongsToStreamUri();
            ContextElement contextElement = null;
            contextElement = IotBrokerDataClient.createContextElement(observation);
            contextElements.add(contextElement);
        }
        return updateContext(contextElements);
    }

    public void deleteSubscription(String subscriptionId) throws Exception {
        UnsubscribeContextRequest request = new UnsubscribeContextRequest(subscriptionId);
        UnsubscribeContextResponse response = iotBrokerClient.unsubscribeContext(request, URI.create(ioTBrokerEndpoint));
        if(response.getStatusCode()!=null && response.getStatusCode().getCode()!=200) {
            throw new Exception("Failed to delete subscription: "+response.getStatusCode().getReasonPhrase());
            //return null;
        }
    }



    @Override
    public String subscribeTo(String entityId, String[] attributes, String referenceUrl, List<NotifyCondition> notifyConditions, Restriction restriction) throws Exception {

        //String id = ioTStream.getURI();
        //String id = StreamObservation.getTypeUri()+"_"+URI.create(ioTStream.getURI()).getFragment();
        //ContextElement contextElement = createContextElement(ioTStream);
        CustomSubscribeContextRequest request = new CustomSubscribeContextRequest();
        request.setReference(referenceUrl);
        //request.setEntityIdList(Arrays.asList(contextElement.getEntityId()));
        request.setEntityIdList(Arrays.asList(new EntityId(entityId, URI.create(StreamObservation.getTypeUri()), false)));
        //request.setAttributeList(Arrays.asList(new String[]{ "noiseLevel" }));
        //request.setEntityIdList(new EntityId(){{  setId(id); setIsPattern(true); }} );
        request.setAttributeList(Arrays.asList(attributes));

        if(restriction==null)
             restriction = new Restriction();
        //restriction.setAttributeExpression();
        request.setRestriction(restriction);

        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        //NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONTIMEINTERVAL, Arrays.asList(new String[]{ "noiseLevel" }), null);

        javax.xml.datatype.Duration duration = null;
        try {
            duration = DatatypeFactory.newInstance().newDuration("P30DT15H45M0S");
            request.setDuration(duration);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        request.setNotifyCondition(notifyConditions);

        SubscribeContextResponse response = iotBrokerClient.subscribeContext(request, URI.create(ioTBrokerEndpoint));
        if(response.getSubscribeError()!=null && response.getSubscribeError().getStatusCode()!=null && response.getSubscribeError().getStatusCode().getCode()!=200) {
            LOGGER.error(response.getSubscribeError().getStatusCode().toJsonString());
            throw new Exception(response.getSubscribeError().getStatusCode().toJsonString());
        }

        String subscriptionId = response.getSubscribeResponse().getSubscriptionId();
        return subscriptionId;
    }

    private Boolean updateContext(List<ContextElement> contextElements){
        UpdateContextRequest request = new UpdateContextRequest();
        request.setContextElement(contextElements);
        request.setUpdateAction(UpdateActionType.APPEND);
        //LOGGER.debug(request.toJsonString());
        UpdateContextResponse response = iotBrokerClient.updateContext(request, URI.create(ioTBrokerEndpoint));
        if(response.getErrorCode().getCode()!=200) {
            LOGGER.error(response.getErrorCode().toJsonString());
            return false;
        }
        return true;
    }



    @Override
    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception {
        List<StreamObservation> ret = new ArrayList<>();

        QueryContextRequest request = new QueryContextRequest();
        request.setEntityIdList(Arrays.asList(new EntityId[]{
                new EntityId(){{
                                setId(streamId);
                                setIsPattern(false);
                                }}
        }));


        QueryContextResponse response = iotBrokerClient.queryContext(request, URI.create(ioTBrokerEndpoint));
        List<ContextElementResponse> responses = response.getListContextElementResponse();

        for(ContextElementResponse response1 : responses) {
            ContextElement contextElement = response1.getContextElement();
            StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
            ret.add(streamObservation);
        }
        return ret;
    }

    public List<StreamObservation> getObservations(int limit) throws Exception {
       return getObservations(".*",0);
    }

    public List<StreamObservation> getObservations() throws Exception {
        return getObservations(0);
    }

}
