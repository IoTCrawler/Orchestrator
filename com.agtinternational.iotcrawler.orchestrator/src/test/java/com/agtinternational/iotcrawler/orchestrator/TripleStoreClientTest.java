package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.agtinternational.iotcrawler.core.ontologies.IotStream;
import com.agtinternational.iotcrawler.orchestrator.clients.TripleStoreClient;
import com.agtinternational.iotcrawler.core.models.*;
import org.apache.jena.atlas.web.auth.SimpleAuthenticator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDF;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;



public class TripleStoreClientTest {

    TripleStoreClient tripleStoreClient;

    @Before
    public void init(){
        EnvVariablesSetter.init();
        tripleStoreClient = new TripleStoreClient(System.getenv(Constants.TRIPLE_STORE_URL), new SimpleAuthenticator(System.getenv(Constants.TRIPLE_STORE_USER).toString(), System.getenv(Constants.TRIPLE_STORE_PASS).toCharArray()));
    }

    @Ignore
    @Test
    public void getConceptsByType() throws Exception {
        List<String>  list = tripleStoreClient.queryConceptsByType(IoTStream.getTypeUri(), 0);
        String abc = "abc";
    }



    @Test
    public void updateQueryTest() throws Exception {
        String queryString = "INSERT {<http://tripleStoreClient.com/#testInstance> <"+ RDF.type+"> <"+ Constants.sensorURI+"> .}";

        tripleStoreClient.execUpdateQuery(queryString);
        String abc = "abc";
    }

    @Test
    public void insertModelTest() throws Exception {
        IoTStream ioTStream = new IoTStream( IotStream.NS+"#testStream");
        ioTStream.addProperty(IotStream.NS+"#protocol", "dhcp");
        tripleStoreClient.insertModel(ioTStream.getModel());

        Model ret = tripleStoreClient.queryModel(URI.create(IotStream.NS+"#testStream"));
        String abc = "abc";
    }

    @Test
    public void queryModelTest() throws Exception {
        //IoTStream ioTStream = new IoTStream(agtIoTC+"#testStream");
        //List<Pair<RDFNode, RDFNode>> ret = tripleStoreClient.queryAllTriplesOfAResource(agtIoTC+"#testStream");
        Model ret = tripleStoreClient.queryModel(URI.create(IotStream.NS+"#testStream"));
        String abc = "abc";
    }

    @Test
    public void queryModel2Test() throws Exception {
        //IoTStream ioTStream = new IoTStream(agtIoTC+"#testStream");
        //List<Pair<RDFNode, RDFNode>> ret = tripleStoreClient.queryAllTriplesOfAResource(agtIoTC+"#testStream");
        Map<String, Model> ret = tripleStoreClient.queryModel("SELECT distinct ?platformURI ?p ?o WHERE { \n" +
                "  ?platformURI a <http://www.w3.org/ns/sosa/Platform> .\n" +
                "  ?platformURI ?p ?o .\n" +
                "  ?sensorURI <http://www.w3.org/ns/sosa/isHostedBy> ?platformURI . }");
        String abc = "abc";
    }

}
