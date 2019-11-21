//package com.agtinternational.iotcrawler.orchestrator;

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
//
//import org.apache.commons.io.Charsets;
//import org.apache.commons.lang3.tuple.Pair;
//import org.apache.jena.ontology.OntModel;
//import org.apache.jena.rdf.model.*;
//import org.apache.jena.riot.Lang;
//import org.apache.jena.riot.RDFDataMgr;
//import org.apache.jena.vocabulary.RDF;
//
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.net.URI;
//import java.nio.ByteBuffer;
//import java.util.List;
//
//public class Utils {
//
//
////    public static Model createModelFromProperties(String uri, List<Pair<RDFNode, RDFNode>> properties){
////        Model model = ModelFactory.createDefaultModel();
////        Resource resource = model.createResource(uri);
////        for (Pair<RDFNode, RDFNode> pair : properties){
////            Property property = (pair.getKey().asResource().getURI().equals(RDF.type.getURI())? RDF.type: model.createProperty(pair.getKey().asResource().getURI()));
////            try {
////                model.add(resource, property, pair.getValue());
////                //model.add(resource, property, "123");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////        return model;
////    }
//
//
//    public static String writeModel2JsonLDString(Model model) {
//        if (model == null) {
//            return "";
//        } else {
//            StringWriter writer = new StringWriter();
//            RDFDataMgr.write(writer, model, Lang.JSONLD);
//            return writer.toString();
//        }
//    }
//
//    public static String writeModel2RDFString(Model model) {
//        if (model == null) {
//            return "";
//        } else {
//            StringWriter writer = new StringWriter();
//            RDFDataMgr.write(writer, model, Lang.RDFXML);
//            return writer.toString();
//        }
//    }
//
//
//    public static Model readModel(byte data[]) {
//        return readModel(data, 0, data.length);
//    }
//
//
//    public static Model readModel(byte data[], int offset, int length) {
//        return readModel(readString(data, offset, length));
//    }
//
//    public static Model readModel(String string) {
//        StringReader reader = new StringReader(string);
//        Model model = ModelFactory.createDefaultModel();
//        RDFDataMgr.read(model, reader, "", Lang.JSONLD);
//        return model;
//    }
//
//    public static String readString(byte[] data, int offset, int length) {
//        if (data == null) {
//            return null;
//        } else {
//            return new String(data, offset, length, Charsets.UTF_8);
//        }
//    }
//
//    public static String getFragment(String uriString){
//        URI uri = URI.create(uriString);
//        String ret = uri.getFragment();
//        if(ret==null) {
//            String[] splitted = uriString.split("/");
//            ret = splitted[splitted.length-1];
//        }
//        return ret;
//    }
//
//
//}
