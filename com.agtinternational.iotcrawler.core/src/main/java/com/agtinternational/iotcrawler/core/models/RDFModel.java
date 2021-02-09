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
import com.agtinternational.iotcrawler.core.ontologies.IotStream;
import com.agtinternational.iotcrawler.core.ontologies.SOSA;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.impl.LiteralImpl;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.vocabulary.RDFS;

import com.orange.ngsi2.model.Attribute;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

import static com.agtinternational.iotcrawler.core.Constants.*;


public class RDFModel {

    protected static Logger LOGGER = LoggerFactory.getLogger(RDFModel.class);


    protected Model model;

    protected Resource resource;

    protected static Map<String, String> namespaces = new HashMap<>();

    protected URI uri;

    static
    {
        namespaces.put(SOSA.Prefix, SOSA.NS);
        namespaces.put(IotStream.Prefix, IotStream.NS);
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
        setProperty(RDF.type, this.model.createResource(typeUri));
//        Object typeNodes = getProperty(RDF.type.getURI());
//
//        if(typeNodes!=null && typeNodes.size()>0)
//            this.model.remove(resource, RDF.type, typeNodes.get(0));
//
//        addProperty(RDF.type, this.model.createResource(typeUri));
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

    public void label(String value) {
        setProperty(RDFS.label, value);
    }

    public String getTypeURI() throws Exception {
        //String ret = model.getResource(RDF.type.getURI()).toString();
        //return ret;
        Object res = getProperty(RDF.type.toString());
        if(res==null)
            throw new Exception("RDF Type not found");

        RDFNode attributeNode = ((RDFNode) res);
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
//    public void setProperty(String uri, Object value){
//        Property property = model.createProperty(uri);
//        addProperty(property, value);
//    }

    public void addProperty(String uri, Object value){
        //if(Utils.getFragment(uri)!=null && URI.create(uri).getFragment().toLowerCase().equals("label"))
          //  uri = RDFS.label.getURI();
        Property property = model.getProperty(uri);
        if(property==null)
            property =  model.createProperty(uri);
        if(value.getClass().isArray())
            value = Arrays.asList((Object[])value);

        if(value instanceof Iterable){
          Iterator iterator = ((Iterable) value).iterator();
          while (iterator.hasNext())
              addProperty(property, iterator.next());
        }else
            addProperty(property, value);
    }

    public void addProperty(Property property, Object value){
        Statement statement = model.getProperty(resource, property);
        RDFNode alreadyExistingValue = (statement!=null? statement.getObject(): null);

        if(value.equals(alreadyExistingValue))
            return;

//        if(alreadyExistingValue!=null) {
//            List valueAsList = new ArrayList<>();
//            if(alreadyExistingValue instanceof Iterable)
//                valueAsList.addAll(Arrays.asList(alreadyExistingValue));
//            else
//                valueAsList.add(alreadyExistingValue);
//            value = valueAsList;
//        }
        if (value instanceof RDFModel){
            //resource.addProperty(property, ((RDFModel) value).resource);
            //model.add(((RDFModel) value).getModel());
            //Literal urlAsLiteral = model.createTypedLiteral(((RDFModel)value).getURI(), XSDDatatype.XSDstring);
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

    public void setProperty(String uri, Object value){
        Property property = new PropertyImpl(uri);
        setProperty(property, value);
    }

    public void setProperty(Property property, Object value){
        removeProperty(property);
        addProperty(property, value);
    }

    public void removeProperty(String uri){
        Property property = new PropertyImpl(uri);
        removeProperty(property);
    }

    public void removeProperty(Property property){
        Object value = getProperty(property.getURI());
        if(value!=null)
            if(value instanceof Iterable) {
                Iterator iterator = ((Iterable)value).iterator();
                    while (iterator.hasNext()) {
                        Object valueItem = iterator.next();
                        if(valueItem instanceof RDFNode)
                            removePropertyValue(property, (RDFNode) valueItem);
                        else
                            throw new NotImplementedException(valueItem.getClass().getCanonicalName());
                    }
            }else if(value instanceof RDFNode)
                removePropertyValue(property, (RDFNode)value);
            else
                throw new NotImplementedException(value.getClass().getCanonicalName());

    }

//    public void removeProperty(String uri, String value){
//        removePropertyValue(property, );
//    }

    public void removePropertyValue(String propertyUri, RDFNode value){
        removePropertyValue(model.createProperty(propertyUri), value);
    }
    public void removePropertyValue(Property property, RDFNode value){

        Statement statement = new StatementImpl(resource, property, value);
//        Boolean remove = false;
//        if(value!=null) {
//            if (existingValue.isLiteral()) {
//                if(existingValue.asLiteral().toString().equals(value))
//                    remove = true;
//            } else if (existingValue.isResource()) {
//                if(existingValue.asResource().getURI().equals(value))
//                    remove = true;
//            } else throw new NotImplementedException(existingValue.getClass().getCanonicalName());
//        }else
//            remove = true;

        model.remove(statement);
    }

    public List<String> getAttributeNames(){
        Set<String> ret = getProperties(null).keySet();
        //return ret.toArray(new String[0]);
        return new ArrayList(ret);
    }

    public Map<String, List<Object>> getAttributes(){
        return getProperties(null);
    }

    public Object getProperty(String uri){
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
        if(ret.isEmpty())
            return null;
        if(ret.size()==1)
            return ret.get(0);

        return ret;
    }

    public Map<String, List<Object>> getProperties(){
        return getProperties(null);
    }

    public Object getAttribute(String uri){
        Object propertyValue = getProperty(uri);
        List<String> ret = new ArrayList<>();

        if(propertyValue instanceof Iterable){
            Iterator iterable = ((Iterable)propertyValue).iterator();
            while (iterable.hasNext()){
                Object value = iterable.next();
                if (value instanceof Resource)
                    ret.add(((Resource)value).asResource().toString());
                else if (value instanceof Literal)
                    ret.add(((Literal)value).asLiteral().getString());
                else
                    throw new NotImplementedException(value.getClass().getCanonicalName());
            }
            if(ret.size()==0)
                return null;

            if(ret.size()==1)
                return ret.get(0);

            return ret;
        }else{
            if (propertyValue instanceof Resource)
                return ((Resource)propertyValue).asResource().toString();
            else if (propertyValue instanceof Literal)
                return  ((Literal)propertyValue).asLiteral().getString();
            else
                throw new NotImplementedException(propertyValue.getClass().getCanonicalName());
        }


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

            String[] gridSplitted = attributeKey.split("#");
            if(gridSplitted.length>1){
                try {
                    int parsed = Integer.parseInt(gridSplitted[gridSplitted.length - 1]);
                    List<String> list = Arrays.asList(gridSplitted);
                    attributeKey = String.join("#", list.subList(0, list.size()-1));
                    String abc = "123";
                }
                catch (Exception e){

                }
            }

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
                LOGGER.warn("Type of " + attributeKey +" attribute is not declared as URI. Impossible to add this property");
                //throw new Exception("Type of " + attributeKey +" attribute is not declared as URI");

            //Property property = model.createProperty(attrTypeUri);
            /*
            if(attribute instanceof Relationship) {
                Resource resource2 = model.createResource(attribute.getId());
                for (String key2 : ((Relationship) attribute).getProperties().keySet())
            }
            */
            else {
                if (attribute instanceof Iterable) {
                    Iterator iterator = ((Iterable) attribute).iterator();
                    while (iterator.hasNext()) {
                        Object attribute2 = iterator.next();
                        Object value2 = getValueFromAttribute(attribute2, ret.getModel());
                        ret.addProperty(attrTypeUri, value2);
                    }
                    String test = "123";
                } else {
                    Object value = getValueFromAttribute(attribute, ret.getModel());
                    ret.addProperty(attrTypeUri, value);
                }
            }

        }

        Object context = ((EntityLD) entity).getContext();
        //if(context instanceof Map)
//            for(String key: context.keySet()) {
//                Object value = context.get(key);
//                if(value instanceof String) { //works only if
//                    //model.setNsPrefix(key, value.toString());
//                }
//
////                if(value instanceof Map) {
////                    JsonObject jsonObject = EntityLD.mapToJson((Map) value);
////                    model.setNsPrefix(key, jsonObject.toString());
////                }
//            }

        return ret;
    }

    private static Object getValueFromAttribute(Object attribute, Model model){
        if(attribute instanceof Relationship){
            String value = ((Relationship) attribute).getObject().toString();
            Resource ret = model.createResource(value);
            return ret;
        }

        if(attribute instanceof Attribute)
            return ((Attribute) attribute).getValue();

        return attribute;
//        if(attribute instanceof String)
//            return attribute.toString();
//
//        throw new NotImplementedException(attribute.getClass().getSimpleName());
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

        EntityLD ret = new EntityLD(getURI(), type);

        //HashMap<String, Integer> propertiesCounter = new HashMap<>();
        StmtIterator iterator = this.model.listStatements();
        Map<String, Object> attributes = new HashMap<String, Object>();
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
                ((Relationship) attribute).setObject(statement.getObject().asResource().getURI());
            }else if (statement.getObject().isLiteral()){
                attribute  = new com.agtinternational.iotcrawler.fiware.models.NGSILD.Property();
                attribute.setValue(statement.getObject().asLiteral().getValue());
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
            ret.addAttribute(attrName, (Object)attribute);
//            if(attributes.containsKey(attrName)){ // handling multiple values
////                 String newAttName = attrName+"#"+ (propertiesCounter.get(attrName)+1);
////                Attribute attribute2 = null;
////                if(statement.getObject().isResource()){
////                    attribute2 = new Relationship();
////                    ((Relationship) attribute2).setObject(statement.getObject().asResource().getURI());
////                }else if (statement.getObject().isLiteral()){
////                    attribute2  = new com.agtinternational.iotcrawler.fiware.models.NGSILD.Property();
////                    attribute2.setValue(statement.getObject().asLiteral().toString());
////                }else{
////                    throw new NotImplementedException("");
////
////                }
//
//                 Object property =  attributes.get(attrName);
//                 Object value = property.getValue();
//                 if (value instanceof List)
//                     ((List)(value)).add(attribute.getValue());
//                 else{
//                     List list = new ArrayList();
//                     list.add(value);
//                     list.add(attribute.getValue());
//                     property.setValue(list);
//                 }
//                attributes.put(newAttName, attribute2);
//            }else
//                attributes.put(attrName, attribute);

//            int count = (propertiesCounter.containsKey(attrName)?propertiesCounter.get(attrName):0);
//            count++;
//            propertiesCounter.put(attrName, count);
            //attributes.put(attrName, attValues);

        }


        Map<String, Object> context = new HashMap<>();
        String jsonStr = toJsonLDString();

        for(String key: namespaces.keySet())
            context.put(key, namespaces.get(key));

        JsonObject parsed = (JsonObject)new JsonParser().parse(jsonStr);
        if(parsed.get("@context") instanceof JsonObject){
            JsonObject contextObject = (JsonObject)parsed.get("@context");
            for(String key: contextObject.keySet())
                context.put(key, contextObject.get(key));
            //context = new Gson().fromJson(parsed.get("@context").toString(), HashMap.class);

        }


        //EntityLD ret = new EntityLD(getURI(), type, attributes, context);
        ret.setContext(context);
        return ret;
    }


}
