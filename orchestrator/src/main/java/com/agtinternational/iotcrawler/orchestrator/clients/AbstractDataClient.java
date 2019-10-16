package com.agtinternational.iotcrawler.orchestrator.clients;

import com.agtinternational.iotcrawler.core.models.StreamObservation;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;


import java.util.List;

public interface AbstractDataClient {
    public Boolean pushObservations(List<StreamObservation> observation) throws Exception;
    public String subscribeTo(String streamUri, String[] attributes, String referenceUrl, List<NotifyCondition> notifyConditions, Restriction restriction) throws Exception;
    public void deleteSubscription(String subscriptionId) throws Exception;
    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception;
    //public List<StreamObservation> getObservations(int limit) throws Exception;
}
