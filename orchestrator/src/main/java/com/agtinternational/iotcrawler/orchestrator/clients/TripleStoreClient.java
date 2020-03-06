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

import com.agtinternational.iotcrawler.orchestrator.FilteringSelector;
import com.agtinternational.iotcrawler.orchestrator.SparqlQueries;
import org.aksw.jena_sparql_api.core.UpdateExecutionFactory;
import org.aksw.jena_sparql_api.core.UpdateExecutionFactoryHttp;
import org.aksw.jena_sparql_api.core.utils.UpdateRequestUtils;
import org.apache.jena.atlas.web.auth.SimpleAuthenticator;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.update.UpdateRequest;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.core.DatasetDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static com.agtinternational.iotcrawler.orchestrator.Constants.*;


public class TripleStoreClient  {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripleStoreClient.class);
    private QueryExecutionFactoryHttp queryExecFactory;
    private UpdateExecutionFactory updateExecFactory;
    private final Semaphore queryFinishedMutex;

//    public TripleStoreClient(){
//        this(defaultTripleStoreURI)
//    }

    public TripleStoreClient(){
        this(System.getenv(TRIPLE_STORE_URL), new SimpleAuthenticator(System.getenv(TRIPLE_STORE_USER).toString(), System.getenv(TRIPLE_STORE_PASS).toCharArray()));
    }

    public TripleStoreClient(String endpointAddress, HttpAuthenticator httpAuthenticator){
        queryFinishedMutex = new Semaphore(0);
        queryExecFactory = new QueryExecutionFactoryHttp(endpointAddress,  new DatasetDescription(), httpAuthenticator);
        updateExecFactory = new UpdateExecutionFactoryHttp(endpointAddress, httpAuthenticator);
    }

    public void execQuery(String queryString, Function<ResultSet, Void> onSuccess){
        QueryExecution qe = queryExecFactory.createQueryExecution(queryString);
        ResultSet results = null;

        results = qe.execSelect();
        onSuccess.apply(results);
    }

    public List<String> queryConceptsByType(String typeURI, int limit){
        FilteringSelector selector = new FilteringSelector.Builder().predicate("a").object(typeURI).build();
        return queryURIsByFilter(selector, limit);
    }

    public List<String> queryURIsByFilter(FilteringSelector selector, int limit){
        final List<String> ret = new ArrayList<String>();

        //.getObject()
//        if(typeURI.startsWith("http")){
//            typeURI="<"+typeURI+">";
//        }
//        String query = "SELECT ?s ?p ?o { ?s a "+typeURI+" . } "+(limit>0?" LIMIT "+limit:"");

        StringBuilder query = new StringBuilder();
        query.append("SELECT ?s ?p ?o { ");

        query.append((selector.getSubject()!=null? (selector.getSubject().startsWith("http:")?"<"+selector.getSubject()+">":selector.getSubject()):"?s"));
        query.append(" "+(selector.getPredicate()!=null?(selector.getPredicate().startsWith("http:")?"<"+selector.getPredicate()+">":selector.getPredicate()):"?p")+" ");
        query.append((selector.getObject()!=null?(selector.getObject().startsWith("http:")?"<"+selector.getObject()+">":selector.getObject()):"?o"));
        query.append(" . } ");

        if(limit>0)
            query.append(" LIMIT "+limit);

        //"SELECT ?s ?p ?o { ?s a <"+typeURI+"> . } "+(limit>0?" LIMIT "+limit:"");
        execQuery(query.toString(), new Function<ResultSet, Void>() {
            @Override
            public Void apply(ResultSet resultSet) {
                List<String> names = getResultSetAsStringList(resultSet, "?s", false);
                ret.addAll(names);
                queryFinishedMutex.release();
                return null;
            }
        });
        try {
            queryFinishedMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

//    public Model queryModel(String uri){
//        List<Pair<RDFNode, RDFNode>> statements = queryAllTriplesOfAResource(uri);
//        Model ret = ModelFactory.createDefaultModel();
//        for(Pair<RDFNode, RDFNode> pair: statements) {
//            ret.add();
//        }
//    }



    public Model queryModel(URI uri){
        String query = "SELECT ?s ?p ?o WHERE { ?s ?p ?o . FILTER (?s=<"+uri.toString()+">) }";

        Map<String, Model> models = queryModel(query);
        if(models.size()>0)
            return new ArrayList<>(models.values()).get(0);

        return null;
    }

    public Map<String, Model> queryModel(String query){
        Map<String, Model> ret = new TreeMap<>();

        execQuery(query, new Function<ResultSet, Void>() {
            @Override
            public Void apply(ResultSet resultSet) {
                Map<String, Model> models = getResultSetAsModels(resultSet);
                ret.putAll(models);
                queryFinishedMutex.release();
                return null;
            }
        });
        try {
            queryFinishedMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<Pair<RDFNode, RDFNode>> queryAllTriplesOfAResource(String uri){
        final List<Pair<RDFNode, RDFNode>> ret = new ArrayList<>();

        String query = "SELECT * { <"+uri+"> ?p ?o . }";

        execQuery(query.toString(), new Function<ResultSet, Void>() {
            @Override
            public Void apply(ResultSet resultSet) {
                //String test="123";
                //List<Pair<RDFNode, RDFNode>> properties = getResultSetAsModels(resultSet, new String[]{"?p","?o"}, false);
                List<Pair<RDFNode, RDFNode>> properties = getResultSetAsPairsList(resultSet, new String[]{"?p","?o"}, false);

                ret.addAll(properties);
                queryFinishedMutex.release();
                return null;
            }
        });
        try {
            queryFinishedMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }


    public void insertModel(Model model){
        updateModel(SparqlQueries.EMPTY_MODEL, model);
    }



    public Boolean updateModel(Model originalModel, Model model){
        String[] updateQueries = SparqlQueries.getUpdateQueriesFromDiff(originalModel, model, "http://graphURI");
        for(String query: updateQueries)
            execUpdateQuery(query);
        return true;
    }

    public void execUpdateQuery(String queryString){
//        queryString = queryString.replaceFirst("INSERT DATA", "INSERT");
        if(!queryString.toLowerCase().contains("where"))
            queryString += "WHERE { }\n";
        UpdateRequest updateRequest = UpdateRequestUtils.parse(queryString);
        try {
            updateExecFactory.createUpdateProcessor(updateRequest).execute();
        } catch (Exception e) {
            LOGGER.error("Failed to execute execUpdateQuery request: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }


        //}
    }

    public List<Pair<RDFNode, RDFNode>> getIndividualRefsAndTheirClasses(String classUri, boolean recursive) {

        List<Pair<RDFNode, RDFNode>> results = new ArrayList<Pair<RDFNode, RDFNode>>();
        execQuery("SELECT * WHERE {\n" +
                        "  ?class_ref rdfs:subClassOf"+(recursive?"*":"")+" <" + classUri + "> .\n" +
                        "  ?class_ref rdfs:subClassOf ?parent .\n" +
                        "  ?class_ref rdf:type ?type .\n" +
                        "  FILTER (?type!=owl:Class)\n" +
                        "        } ORDER BY DESC (?parent)",
                new Function<ResultSet, Void>() {
                    @Override
                    public Void apply(ResultSet resultSet) {
                        results.addAll(getResultSetAsPairsList(resultSet, new String[]{"?class_ref","?parent"}, false));
                        queryFinishedMutex.release();
                        return null;
                    }

                    ;
                });
        try {
            queryFinishedMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Map<String, Map> getClassHierarchy(String targetClassUri, boolean includeTopLevel){

        List<Pair<RDFNode, RDFNode>> results = new ArrayList<Pair<RDFNode, RDFNode>>();
        execQuery("SELECT * WHERE {\n" +
                        "  ?class_ref rdfs:subClassOf* <"+targetClassUri+"> .\n" +
                        "  ?class_ref rdfs:subClassOf ?parent .\n" +
                        "  ?class_ref rdf:type ?type .\n" +
                        "  FILTER (?type!=owl:NamedIndividual)\n"+
                        "        } ORDER BY DESC (?parent)",
                new Function<ResultSet, Void>(){
                    @Override
                    public Void apply(ResultSet resultSet) {
                        results.addAll(getResultSetAsPairsList(resultSet, new String[]{"?class_ref","?parent"}, false));
                        queryFinishedMutex.release();
                        return null;
                    };
                });
        try {
            queryFinishedMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, String> index = new TreeMap();
        for(Pair<RDFNode, RDFNode> pair : results){
            String classUri = pair.getKey().asResource().getURI();
            String parentUri = pair.getValue().asResource().getURI();
            if(classUri!=null && parentUri!=null)
                index.put(classUri, parentUri);
        }


        Map<String, Map> ret = new TreeMap();
        int iterations=0;
        while (index.size()>0 && iterations<30){

            for (String uri : index.keySet().toArray(new String[0])){
                String parentUri = index.get(uri);
                Map parentLevel = findInHierarchy(parentUri, ret);
                if(parentLevel==null && parentUri.equals(targetClassUri)){
                    parentLevel = new TreeMap();
                    ret.put(parentUri, parentLevel);
                }
                if (parentLevel!=null){
                    //Map<String, Map> subCats = (ret.containsKey(parentUri) ? ret.get(parentUri) : new TreeMap<>());
                    parentLevel.put(uri, new TreeMap<>());
                    //ret.put(parentUri, subCats);
                    index.remove(uri);
                }else{
                    String abc="123";
                }
            }
            iterations++;
        }

        if(!includeTopLevel){
            Map<String, Map> ret2 = new TreeMap();
            ret2.putAll(ret.get(targetClassUri));
            ret = ret2;
        }

        return ret;
    }

    private Map findInHierarchy(String key, Map<String, Map> hierarchy){
        if(hierarchy.containsKey(key))
            return hierarchy.get(key);
        else if (hierarchy.size()>0){
            Map return2 = null;
            for(String hkey: hierarchy.keySet())
                if(hierarchy.get(hkey)!=null && hierarchy.get(hkey).size()>0) {
                    Map ret2 = findInHierarchy(key, hierarchy.get(hkey));
                    if(ret2!=null)
                        return2 = ret2;
                }
            return return2;
        }
        return null;
    }

    public static Map<String, Model>  getResultSetAsModels(ResultSet resultSet) {
        Map<String, Model> ret = new LinkedHashMap<>();
        RDFNode node;
        if (resultSet != null) {
            while (resultSet.hasNext()) {
                QuerySolution result = resultSet.next();

                if (result != null) {
                    List<RDFNode> resultNodes = new ArrayList<>();
                    for(String var: resultSet.getResultVars())
                        resultNodes.add(result.get(var));

                    try {
                        Resource subject = resultNodes.get(0).asResource();
                        Model model = (ret.containsKey(subject.getURI()) ? ret.get(subject.getURI()) : ModelFactory.createDefaultModel());

                        Property predicate = model.createProperty(resultNodes.get(1).asResource().getURI());
                        RDFNode object = resultNodes.get(2);
                        Statement statement = new StatementImpl(subject, predicate, object);

                        model.add(statement);
                        ret.put(subject.getURI(), model);
                    }catch (Exception e){
                        LOGGER.error("Failed to process result");
                    }
                }
            }
        }
        return ret;
    }

    public static List<Pair<RDFNode, RDFNode>> getResultSetAsPairsList(ResultSet resultSet, String[] variableNames, boolean literalRequired) {
        List<Pair<RDFNode, RDFNode>> ret = new ArrayList<>();
        RDFNode node;
        if (resultSet != null) {
            while (resultSet.hasNext()) {
                QuerySolution result = resultSet.next();
                if (result != null) {
                    List<RDFNode> resultStrings = new ArrayList<>();
                    for(String variableName: variableNames){
                        node = result.get(variableName);
                        resultStrings.add(node);
                    }
                    Pair<RDFNode, RDFNode> keyValuePair = new ImmutablePair<>(resultStrings.get(0), resultStrings.get(1));
                    ret.add(keyValuePair);
                }
            }
        }
        return ret;
    }

    public static List<String> getResultSetAsStringList(ResultSet resultSet, String variableName, boolean literalRequired) {
        List<String> resultStrings = new ArrayList<>();
        RDFNode node;
        if (resultSet != null) {
            while (resultSet.hasNext()) {
                QuerySolution result = resultSet.next();
                if (result != null) {
                    node = result.get(variableName);
                    if (literalRequired) {
                        String literal = getStringLiteral(node);
                        if (literal != null) {
                            resultStrings.add(literal);
                        }
                    } else {
                        if (node != null) {
                            resultStrings.add(node.toString());
                        }
                    }
                }
            }
        }
        return resultStrings;
    }

    public static String getStringLiteral(RDFNode node) {
        String literal = null;
        if (node != null && node.isLiteral()) {
            literal = ((Literal) node).getLexicalForm();
        }
        return literal;
    }
}
