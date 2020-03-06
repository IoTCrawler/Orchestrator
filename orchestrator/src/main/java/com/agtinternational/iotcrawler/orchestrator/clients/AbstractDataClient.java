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
