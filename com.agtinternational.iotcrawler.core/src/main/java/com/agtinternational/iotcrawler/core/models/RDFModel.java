package com.agtinternational.iotcrawler.core.models;

/*-
 * #%L
 * core
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

import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.vocabulary.RDFS;

import com.orange.ngsi2.model.Attribute;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

import static com.agtinternational.iotcrawler.core.Constants.iotcNS;
import static com.agtinternational.iotcrawler.core.Constants.sosaNS;


public class RDFModel {

    protected static Logger LOGGER = LoggerFactory.getLogger(RDFModel.class);
    protected URI uri;
    protected Model model;
    protected Resource resource;
    protected static Map<String, String> namespaces = new HashMap<>();


    static
    {
        namespaces.put("sosa", sosaNS);
        namespaces.put("iotc", iotcNS);
        namespaces.put("rdfs", RDFS.getURI());
    }



    public RDFModel(String uri){
        this.uri = URI.create(uri);
        model = ModelFactory.createDefaultModel();
        resource = model.createResource(uri);
    }

    public RDFModel(String uri, String typeURI){
        this(uri);
        setType(typeURI);
    }

//    public RDFModel(String uri, Model model){
//        this(uri);
//        this.model = model;
//    }

    public RDFModel(String uri, String typeURI, Model model){
        this(uri, typeURI);
        this.model = model;
    }



    public void setModel(Model model) {
        this.model = model;
    }

    public void setType(String typeUri){
        List<RDFNode> typeNodes = getProperty(RDF.type.getURI());
        if(typeNodes!=null && typeNodes.size()>0)
            this.model.remove(resource, RDF.type, typeNodes.get(0));

        addProperty(RDF.type, this.model.createResource(typeUri));
    }

    public String getURI() {
        return uri.toString();
    }

    public String getShortname(){
        return URLDecoder.decode(Utils.getFragment(uri.toString()));
//        if(uri.getFragment()!=null)
//            return URLDecoder.decode(uri.getFragment());
//        String[] splitted = uri.toString().split("/");
//        return splitted[splitted.length-1];
    }
    public Model getModel() {
        return model;
    }

    public String label() {
        return (String)getAttribute(RDFS.label.getURI());
    }

    public String getTypeURI(){
        //String ret = model.getResource(RDF.type.getURI()).toString();
        //return ret;
        RDFNode attributeNode = getProperty(RDF.type.toString()).get(0);
        return attributeNode.asResource().getURI();
    }

    public static Map<String, String> getNamespaces() {
        return namespaces;
    }

    public Resource getResource(){
        return resource;
    }

//    public String getTypeURI(){
//        String ret = agtSmartHome+"/"+getClass().getSimpleName();
//        return ret;
//    }
    public String toJsonLDString(){
        String jsonLDString = Utils.writeModel2JsonLDString(model);
        return jsonLDString;
    }

    public JsonObject toJsonObject(){
        String jsonLDString = toJsonLDString();

        //Object jsonld = JSONLD.fromRDF(yourInput, new YourRDFParser());
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(jsonLDString);

        //@id & @type are required to remain with @

        if(jsonObject.has("@context")) {
            JsonObject context = (JsonObject) jsonObject.get("@context");
            for(String contextElementName : context.keySet()){
                Object contextElement = context.get(contextElementName);
                if(contextElement instanceof JsonObject)
                    ((JsonObject)contextElement).addProperty("@type", "@id");
            }
        }
        return jsonObject;
    }

//    public String toJsonString(){
//        JsonObject jsonObject = toJsonObject();
//        return Utils.prettyPrint(jsonObject);
//    }


    public String toRDFString(){
        return Utils.writeModel2RDFString(model);
    }

//    public void setClass(String typeURI){
//        model.add(resource, RDF.type, model.createResource(typeURI));
//    }

    public void addProperty(String uri, Object value){
        //if(Utils.getFragment(uri)!=null && URI.create(uri).getFragment().toLowerCase().equals("label"))
          //  uri = RDFS.label.getURI();

        Property property = model.createProperty(uri);
        if(value.getClass().isArray())
            value = Arrays.asList((Object[])value);

        if(value instanceof Iterable) {
          Iterator iterator = ((Iterable) value).iterator();
          while (iterator.hasNext())
              addProperty(property, iterator.next());
        }else
            addProperty(property, value);
    }

    public void addProperty(Property property, Object value){
        if (value instanceof RDFModel){
            //resource.addProperty(property, ((RDFModel) value).resource);
            //model.add(((RDFModel) value).getModel());
            model.add(resource, property, ((RDFModel)value).getResource());
        }else if (value instanceof Resource) {
            model.add(resource, property, (Resource)value);
        } else if (value instanceof Literal) {
            model.addLiteral(resource, property, (Literal) value);
        } else if(value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof JsonPrimitive){

            if(value instanceof JsonPrimitive){

                JsonPrimitive jsonPrimitive = (JsonPrimitive)value;

                if(jsonPrimitive.isString())
                    value = jsonPrimitive.getAsString();

                else if (jsonPrimitive.isBoolean())
                    value = jsonPrimitive.getAsBoolean();
                else if(jsonPrimitive.isNumber()) {
                    Number number = jsonPrimitive.getAsNumber();
                    if (number instanceof Double)
                        value = jsonPrimitive.getAsDouble();
                    else if (number instanceof Float)
                        value = jsonPrimitive.getAsFloat();
                    else if (number instanceof Long)
                        value = jsonPrimitive.getAsLong();
                    else
                        value = jsonPrimitive.getAsInt();
                }
            }

            XSDDatatype xsdDatatype = getXSD(value);
            Literal literal = model.createTypedLiteral(value, xsdDatatype);
            model.addLiteral(resource, property, literal);
        }else if(value.getClass().isArray()){
            List value2=  Arrays.asList((Object[])value);
            addProperty(property, value2);
        }else if(value instanceof Iterable) {
            Iterator iterator = ((Iterable)value).iterator();
            while(iterator.hasNext()){
                Object value1 = iterator.next();
                addProperty(property, value1);
            }
        }else{
            throw new NotImplementedException("Add property not implemented for "+ value.getClass().getName());
        }
    }

    public List<String> getAttributeNames(){
        Set<String> ret = getProperties(null).keySet();
        //return ret.toArray(new String[0]);
        return new ArrayList(ret);
    }

    public Map<String, List<Object>> getAttributes(){
        return getProperties(null);
    }

    public List<RDFNode> getProperty(String uri){
        String[] keySplitted = uri.split(":");
        //if uri in in shorten format

        if(namespaces.containsKey(keySplitted[0])) {
            String fullUri = namespaces.get(keySplitted[0]);
            List<String> abc = Arrays.asList(keySplitted).subList(1, keySplitted.length);
            uri =  fullUri+String.join(":", abc);
        }

        List<RDFNode> ret = new ArrayList<>();
        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()){
            Statement statement = iterator.nextStatement();
            if (statement.getSubject().equals(resource) && statement.getPredicate().getURI().equals(uri))
                ret.add(statement.getObject());
        }
        return ret;
    }

    public Map<String, List<Object>> getProperties(){
        return getProperties(null);
    }

    public Object getAttribute(String uri){
        List<RDFNode> nodes = getProperty(uri);
        List<String> ret = new ArrayList<>();
        for(RDFNode node: nodes) {
            if (node instanceof Resource)
                ret.add(node.asResource().toString());
            if (node instanceof Literal)
                ret.add(node.asLiteral().getString());
        }
        if(ret.size()==0)
            return null;

        if(ret.size()==1)
            return ret.get(0);

        return ret;
    }

    public Map<String, List<Object>> getProperties(String uri){
        //List<Pair<String, Object>> ret = new ArrayList<>();
        Map<String, List<Object>> ret = new HashMap<>();

        Resource resource = model.listResourcesWithProperty(RDF.type).next();
        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()){
            Statement statement = iterator.nextStatement();
            if (statement.getSubject().equals(resource))
                if(uri==null || statement.getPredicate().getURI().equals(uri)){
                    //String propName = URI.create(statement.getPredicate().getURI()).getFragment();
                    String propName = statement.getPredicate().getURI();

                    List<Object> values = (ret.containsKey(propName)? ret.get(propName): new ArrayList<>());

                    if (statement.getObject().isLiteral()) {
                        String typeUri = statement.getObject().asLiteral().getDatatypeURI();
                        values.add(statement.getObject().asLiteral().getValue());
                        //ret.add(new ImmutablePair<>(propName, statement.getObject().asLiteral().getString()));
                    }else if (statement.getObject().isResource()) {
                        values.add(statement.getObject().asResource().getURI());
                        //ret.add(new ImmutablePair<>(propName, statement.getObject().asResource().getURI()));
                    }
                    ret.put(propName, values);
                }
        }

        return ret;
    }


    private XSDDatatype getXSD(Object value){
        if(value instanceof String)
            return XSDDatatype.XSDstring;

        if(value instanceof Boolean)
            return XSDDatatype.XSDboolean;

        if(value instanceof Number)
            if (value instanceof Double)
                return XSDDatatype.XSDdouble;
            else if (value instanceof Float)
                return XSDDatatype.XSDfloat;
            else if (value instanceof Long)
                return XSDDatatype.XSDlong;
            else
                return XSDDatatype.XSDinteger;



        throw new NotImplementedException(value.getClass().getName()+" not implemented as XSD");
    }

    public static RDFModel fromEntity(EntityLD entity) throws Exception {
        RDFModel ret = new RDFModel(entity.getId(), entity.getType());
        //return fromJson(jsonString);
        //Model model = ModelFactory.createDefaultModel();
        //Resource resource = model.createResource(entity.getId());
        //model.add(resource, RDF.type, model.createResource(entity.getType()));

        for(String attributeKey : entity.getAttributes().keySet()){
            Object attribute = entity.getAttributes().get(attributeKey);
            String attrTypeUri = null;

            String[] keySplitted = attributeKey.split(":");
            if(keySplitted.length==2) {
                String nsKey = keySplitted[0];
                if(ret.getNamespaces().containsKey(nsKey))
                    attrTypeUri = ret.getNamespaces().get(nsKey)+keySplitted[1];
                else
                    attrTypeUri = attributeKey;
            }else if(entity.getContext()!=null){  //trying to find typeURI in context
                Object context = entity.getContext();
                if(context instanceof Map){
                    Object attributeContext = ((Map)context).get(attributeKey);
                    if (attributeContext instanceof Map) {
                        if (((Map) attributeContext).containsKey("@id"))
                            attrTypeUri = ((Map) attributeContext).get("@id").toString();

                        if (((Map) attributeContext).containsKey("id"))
                            attrTypeUri = ((Map) attributeContext).get("id").toString();

                    } else if (attributeContext != null)
                        attrTypeUri = attributeContext.toString();
                    else {
                        throw new Exception("Context not found for attribute " + attributeKey);
                    }
                }
            }

            if(attrTypeUri==null)
                throw new Exception("Type of " + attributeKey +" attribute is not declared as URI");

            //Property property = model.createProperty(attrTypeUri);
            /*
            if(attribute instanceof Relationship) {
                Resource resource2 = model.createResource(attribute.getId());
                for (String key2 : ((Relationship) attribute).getProperties().keySet())
            }
            */
            if(attribute instanceof Iterable){
                Iterator iterator = ((Iterable) attribute).iterator();
                while(iterator.hasNext()) {
                    Object attribute2 = iterator.next();
                    Object value2 = getValueFromAttribute(attribute2);
                    ret.addProperty(attrTypeUri, value2);
                }
            }else {
                Object value = getValueFromAttribute(attribute);
                ret.addProperty(attrTypeUri, value);
            }

        }

        Map<String, Object> context = (Map)((EntityLD) entity).getContext();
        if(context!=null)
            for(String key: context.keySet()) {
                Object value = context.get(key);
                if(value instanceof String) { //works only if
                    //model.setNsPrefix(key, value.toString());
                }

//                if(value instanceof Map) {
//                    JsonObject jsonObject = EntityLD.mapToJson((Map) value);
//                    model.setNsPrefix(key, jsonObject.toString());
//                }
            }

        return ret;
    }

    private static Object getValueFromAttribute(Object attribute){
        if(attribute instanceof Relationship)
            return ((Relationship) attribute).getObject();

        if(attribute instanceof Attribute)
            return ((Attribute) attribute).getValue();

        throw new NotImplementedException(attribute.getClass().getSimpleName());
    }

    public static RDFModel fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static RDFModel fromJson(String jsonString){
        Model model = Utils.readModel(jsonString.getBytes());

        String uri = "";
        String typeUri = "";
        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()){
            Statement statement = iterator.next();
            if (statement.getPredicate().equals(RDF.type)) {
                uri = statement.getSubject().getURI();
                typeUri = statement.getObject().asResource().getURI();
            }
        }
        return new RDFModel(uri, typeUri, model);
    }

    public static RDFModel fromContextElement(ContextElement contextElement){
        EntityId entityId = contextElement.getEntityId();
        //Model model = ModelFactory.createDefaultModel();
        //Resource resource = model.createResource(entityId.getId());
        //model.add(resource, RDF.type, entityId.getType().toString());

        RDFModel ret = new RDFModel(entityId.getId(), entityId.getType().toString());
        for(ContextAttribute contextAttribute : contextElement.getContextAttributeList()) {
            String name = contextAttribute.getName();
            URI typeUrl = contextAttribute.getType();
            String value = contextAttribute.getContextValue();
            try {
                //Attribute attribute = contextElement.getProperties().get(key);


//                List<ContextMetadata> metadata = contextAttribute.getMetadata();
//                if(metadata!=null){
//                    contextAttribute.getMetadata().get(0).getType();
//                }

                //Literal literal = ret.getModel().createLiteral(contextAttribute.getContextValue(), type.toString());
                Literal literal = ret.getModel().createTypedLiteral(value, typeUrl.toString());
                ret.addProperty(name, literal);
                //model.add(resource, model.createProperty(uri.toString()),  String.valueOf(contextAttribute.getContextValue()), XSDDatatype.XSDstring);

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Failed to add property: {}", e.getLocalizedMessage());
            }
        }
        return ret;
    }


    private String findNsByValue(String value){
        for(String nsKey: namespaces.keySet())
            if(namespaces.get(nsKey).equals(value))
                return nsKey;
        return null;
    }

    public EntityLD toEntityLD() throws Exception{
        return toEntityLD(false);
    }

    public EntityLD toEntityLD(boolean cutURIs) throws Exception {

        String type = getTypeURI();

        if(cutURIs)
            type = Utils.cutURL(type, namespaces);

        StmtIterator iterator = this.model.listStatements();
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            if(statement.getPredicate().equals(RDF.type))
                continue;

            Attribute attribute = null;
            String attrName = statement.getPredicate().getURI();
            if(cutURIs)
                attrName = Utils.cutURL(attrName, namespaces);

            if(statement.getObject().isResource()){
                attribute = new Relationship();
                //attribute.setType();
                //attValues.add(statement.getObject().asResource().getURI());
                ((Relationship) attribute).setObject(statement.getObject().asResource().getURI());
                //Statement typeStatement = statement.getObject().asResource().getProperty(RDF.type);
                //attribute.setType(Optional.of(typeStatement.getSubject().getURI()));
            }else if (statement.getObject().isLiteral()){
                attribute  = new com.agtinternational.iotcrawler.fiware.models.NGSILD.Property();
                //attValues.add(statement.getObject().asResource().getURI());
                attribute.setValue(statement.getObject().asLiteral().toString());
                //attribute.setType(Optional.of(statement.getObject().asLiteral().getDatatypeURI()));
//                    Literal literal = statement.getLiteral();
//                    //List<ContextMetadata> metadata = new ArrayList<ContextMetadata>();//{{ add(new ContextMetadata("units", URI.create("units"), "dB")); }};
//                    //List<ContextMetadata> metadata = null;
//                    //ContextAttribute contextAttribute = new ContextAttribute(pair.getKey(), URI.create("float"), String.valueOf(System.nanoTime()), metadata);
//                    attribute.setType(Optional.of(literal.getDatatypeURI()));
//                    attribute.setValue(literal.getString());
////                ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getLocalName(), URI.create(literal.getDatatypeURI()), literal.getString(), metadata);
////                contextAttributeList.add(contextAttribute);
//                    attributes.put(statement.getPredicate().getLocalName(), attribute);
            }else{
                throw new NotImplementedException("");

            }
            // else if (statement.getPredicate().hasURI(Constants.belongsTo)) {
////                ContextAttribute contextAttribute = new ContextAttribute(statement.getPredicate().getLocalName(), URI.create(iotStreamURI), statement.getObject().asResource().getURI(), null);
////                contextAttributeList.add(contextAttribute);
//                    attribute.setType(Optional.of(Constants.iotStreamURI));
//                    attribute.setValue(statement.getObject().asResource().getURI());
//                } else {
//                    //LOGGER.info("Skipped as non literal: {}", statement.getPredicate().getURI());
//                    String abc = "123";
            //}
            //attributes.put(statement.getPredicate().getLocalName(), attribute);
            if(attributes.containsKey(attrName)) {
                 Attribute property =  attributes.get(attrName);
                 Object value = property.getValue();
                 if (value instanceof List)
                     ((List)(value)).add(attribute.getValue());
                 else{
                     List list = new ArrayList();
                     list.add(value);
                     list.add(attribute.getValue());
                     property.setValue(list);
                     //attributes.put(attrName, list);
                     //LOGGER.warn("List insertion not implemented for attributes. Skipping {}", attrName);
                 }
            }else
                attributes.put(attrName, attribute);
            //attributes.put(attrName, attValues);

        }


        Map<String, Object> context = new HashMap<>();
        String jsonStr = toJsonLDString();
        JsonObject parsed = (JsonObject)new JsonParser().parse(jsonStr);
        if(parsed.get("@context") instanceof JsonObject){
            //context = new Gson().fromJson(parsed.get("@context").toString(), HashMap.class);
            for(String NsKey: namespaces.keySet())
                context.put(NsKey, namespaces.get(NsKey));
        }


        EntityLD ret = new EntityLD(getURI(), type, attributes, context);
        return ret;
    }


}