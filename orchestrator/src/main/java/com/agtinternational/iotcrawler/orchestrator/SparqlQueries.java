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

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.modify.request.QuadAcc;
import org.apache.jena.sparql.modify.request.UpdateDeleteInsert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SparqlQueries {

    private static final int DEFAULT_MAX_UPDATE_QUERY_TRIPLES = 200;
    public static final Model EMPTY_MODEL = ModelFactory.createDefaultModel();

    public static final String getUpdateQueryFromDiff(Model original, Model updated, String graphUri) {
        return getUpdateQueryFromStatements(original.difference(updated).listStatements().toList(),
                updated.difference(original).listStatements().toList(),
                original.size() > updated.size() ? original : updated, graphUri);
    }

    /**
     * Generates a SPARQL UPDATE query based on the given list of statements
     * that should be deleted and that should be added in the graph with the
     * given URI.
     *
     * @param deleted
     *            statements that should be deleted from the graph
     * @param inserted
     *            statements that should be added to the graph
     * @param mapping
     *            A prefix mapping used for the query
     * @param graphUri
     *            the URI of the graph which should be updated with the
     *            generated query
     * @return the execUpdateQuery query
     */
    public static final String getUpdateQueryFromStatements(List<Statement> deleted, List<Statement> inserted,
                                                            PrefixMapping mapping, String graphUri) {
        UpdateDeleteInsert update = new UpdateDeleteInsert();
        Node graph = null;
        if (graphUri != null) {
            graph = NodeFactory.createURI(graphUri);
            update.setWithIRI(graph);
        }
        Iterator<Statement> iterator;

        // deleted statements
        iterator = deleted.iterator();
        QuadAcc quads = update.getDeleteAcc();
        while (iterator.hasNext()) {
            quads.addTriple(iterator.next().asTriple());
        }

        // inserted statements
        iterator = inserted.iterator();
        quads = update.getInsertAcc();
        while (iterator.hasNext()) {
            quads.addTriple(iterator.next().asTriple());
        }

        return update.toString(mapping);
    }

    /**
     * Generates one or several SPARQL UPDATE queries based on the differences
     * between the two given models. Triples that are present in the original
     * model but not in the updated model will be put into the DELETE part of
     * the query. Triples that are present in the updated model but can not be
     * found in the original model will be put into the INSERT part of the
     * query. The changes might be carried out using multiple queries if a
     * single query could hit a maximum number of triples.
     *
     * @param original
     *            the original RDF model ({@code null} is interpreted as an
     *            empty model)
     * @param updated
     *            the updated RDF model ({@code null} is interpreted as an empty
     *            model)
     * @param graphUri
     *            the URI of the graph to which the UPDATE query should be
     *            applied or <code>null</code>
     * @return The SPARQL UPDATE query
     */
    public static final String[] getUpdateQueriesFromDiff(Model original, Model updated, String graphUri) {
        return getUpdateQueriesFromDiff(original, updated, graphUri, DEFAULT_MAX_UPDATE_QUERY_TRIPLES);
    }

    /**
     * Generates one or several SPARQL UPDATE queries based on the differences
     * between the two given models. Triples that are present in the original
     * model but not in the updated model will be put into the DELETE part of
     * the query. Triples that are present in the updated model but can not be
     * found in the original model will be put into the INSERT part of the
     * query. The changes will be carried out using multiple queries if a single
     * query would hit the given maximum number of triples per query.
     *
     * @param original
     *            the original RDF model ({@code null} is interpreted as an
     *            empty model)
     * @param updated
     *            the updated RDF model ({@code null} is interpreted as an empty
     *            model)
     * @param graphUri
     *            the URI of the graph to which the UPDATE query should be
     *            applied or <code>null</code>
     * @param maxTriplesPerQuery
     *            the maximum number of triples a single query should contain
     * @return The SPARQL UPDATE query
     */
    public static final String[] getUpdateQueriesFromDiff(Model original, Model updated, String graphUri,
                                                          int maxTriplesPerQuery) {
        if (original == null) {
            original = EMPTY_MODEL;
        }
        if (updated == null) {
            updated = EMPTY_MODEL;
        }
        List<Statement> deleted = original.difference(updated).listStatements().toList();
        List<Statement> inserted = updated.difference(original).listStatements().toList();

        int numberOfDelStmts = deleted.size();
        int totalSize = Math.toIntExact(numberOfDelStmts + inserted.size());
        int queries = (totalSize / maxTriplesPerQuery) + 1;
        String[] results = new String[queries];
        int startIndex = 0;
        int endIndex = Math.min(maxTriplesPerQuery, totalSize);
        List<Statement> delStatements, addStatements;
        List<Statement> emptyList = new ArrayList<>(0);
        for (int i = 0; i < queries; i++) {
            // If we can fill the next query with deleted statements
            if (endIndex < numberOfDelStmts) {
                delStatements = deleted.subList(startIndex, endIndex);
                addStatements = emptyList;
            } else {
                if (startIndex < numberOfDelStmts) {
                    delStatements = deleted.subList(startIndex, numberOfDelStmts);
                    addStatements = inserted.subList(0, endIndex - numberOfDelStmts);
                } else {
                    delStatements = emptyList;
                    addStatements = inserted.subList(startIndex - numberOfDelStmts, endIndex - numberOfDelStmts);
                }
            }
            //if(delStatements.size()>0 || addStatements.size()>0) {
                String query = getUpdateQueryFromStatements(delStatements, addStatements,
                        original.size() > updated.size() ? original : updated, graphUri);
                results[i] = query;
            //}
            // get the indexes of the next query
            startIndex = endIndex;
            endIndex = Math.min(endIndex + maxTriplesPerQuery, totalSize);
        }

        return results;
    }
}
