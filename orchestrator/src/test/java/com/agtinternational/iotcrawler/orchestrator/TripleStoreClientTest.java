package com.agtinternational.iotcrawler.orchestrator;

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

import static com.agtinternational.iotcrawler.core.Constants.iotcNS;

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
        IoTStream ioTStream = new IoTStream(iotcNS+"#testStream");
        ioTStream.addProperty(iotcNS+"#protocol", "dhcp");
        tripleStoreClient.insertModel(ioTStream.getModel());

        Model ret = tripleStoreClient.queryModel(URI.create(iotcNS+"#testStream"));
        String abc = "abc";
    }

    @Test
    public void queryModelTest() throws Exception {
        //IoTStream ioTStream = new IoTStream(agtIoTC+"#testStream");
        //List<Pair<RDFNode, RDFNode>> ret = tripleStoreClient.queryAllTriplesOfAResource(agtIoTC+"#testStream");
        Model ret = tripleStoreClient.queryModel(URI.create(iotcNS+"#testStream"));
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
