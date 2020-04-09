//package com.agtinternational.iotcrawler.orchestrator.clients;
//
///*-
// * #%L
// * orchestrator
// * %%
// * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
// * %%
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * #L%
// */
//
//import com.agtinternational.iotcrawler.core.models.StreamObservation;
//import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
//import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
//
//
//import java.util.List;
//
//public interface AbstractDataClient {
//    public Boolean pushObservations(List<StreamObservation> observation) throws Exception;
//    public String subscribeTo(String streamUri, String[] attributes, String referenceUrl, List<NotifyCondition> notifyConditions, Restriction restriction) throws Exception;
//    public void deleteSubscription(String subscriptionId) throws Exception;
//    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception;
//    //public List<StreamObservation> getObservations(int limit) throws Exception;
//}
