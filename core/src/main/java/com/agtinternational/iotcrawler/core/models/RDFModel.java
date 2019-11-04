package com.agtinternational.iotcrawler.core.models;

import com.agtinternational.iotcrawler.core.OrchestratorRPCClient;
import com.agtinternational.iotcrawler.core.Utils;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.*;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
    protected Map<String, String> namespaces1 = new HashMap<>();

    public RDFModel(){
        namespaces1.put("sosa", sosaNS);
        namespaces1.put("iotc", iotcNS);
        namespaces1.put("rdf-schema", RDFS.getURI());
    }

    public RDFModel(String uri){
        this();
        this.uri = URI.create(uri);
        model = ModelFactory.createDefaultModel();
        resource = model.createResource(uri);
    }

    public RDFModel(String uri, String typeURI){
        this(uri);
        setType(typeURI);
        //model.add(resource, RDF.type, model.createResource(typeURI));
    }

    public RDFModel(String uri, Model model){
        this(uri);
        this.model = model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setType(String typeUri) {
        this.resource.addProperty(RDF.type, this.model.createResource(typeUri));
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

    public String getLabel() {
        RDFNode node = getProperty("rdf-schema:label");
        if(node instanceof Resource)
            return node.asResource().toString();
        if(node instanceof Literal)
            return node.asLiteral().getString();
        return null;
    }

    public String getTypeURI(){
        //String ret = model.getResource(RDF.type.getURI()).toString();
        //return ret;
        RDFNode attributeNode = getProperty(RDF.type.toString());
        return attributeNode.asResource().getURI();
    }

    public Map<String, String> getNamespaces() {
        return namespaces1;
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

        Property property = model.createProperty(uri); //(pair.getKey().asResource().getURI().equals(RDF.type.getURI())? RDF.type: model.createProperty(pair.getKey().asResource().getURI()));
        addProperty(property, value);
    }

    public void addProperty(Property property, Object value){
        if (value instanceof RDFModel){
            resource.addProperty(property, ((RDFModel) value).resource);
            model.add(((RDFModel) value).getModel());
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

    public RDFNode getProperty(String uri){
        return getAttribute(uri);
    }

    public Map<String, List<Object>> getProperties(){
        return getProperties(null);
    }

    public RDFNode getAttribute(String uri){

        String[] keySplitted = uri.split(":");
        //if uri in in shorten format

        if(namespaces1.containsKey(keySplitted[0])) {
            String fullUri = namespaces1.get(keySplitted[0]);
            List<String> abc = Arrays.asList(keySplitted).subList(1, keySplitted.length);
            uri =  fullUri+String.join(":", abc);
        }

        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()){
            Statement statement = iterator.nextStatement();
            if (statement.getSubject().equals(resource) && statement.getPredicate().getURI().equals(uri))
                return statement.getObject();
        }
        return null;
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
        RDFModel ret = new RDFModel(entity.getId());
        //return fromJson(jsonString);
        Model model = ModelFactory.createDefaultModel();
        Resource resource = model.createResource(entity.getId());
        model.add(resource, RDF.type, model.createResource(entity.getType()));

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

            Property property = model.createProperty(attrTypeUri);
            /*
            if(attribute instanceof Relationship) {
                Resource resource2 = model.createResource(attribute.getId());
                for (String key2 : ((Relationship) attribute).getProperties().keySet())
            }
            */
            if(attribute instanceof Relationship){
                model.add(resource, property, model.createResource(String.valueOf(((Relationship) attribute).getObject())));
            }else if(attribute instanceof Attribute) {
                model.add(resource, property, String.valueOf(((Attribute) attribute).getValue()));
            }else if(attribute instanceof Iterable){
                Iterator iterator = ((Iterable) attribute).iterator();
                while(iterator.hasNext()) {
                    Object attribute2 = iterator.next();
                    if(attribute2 instanceof Relationship)
                        model.add(resource, property, model.createResource(String.valueOf(((Relationship) attribute2).getObject())));
                    else
                        model.add(resource, property, String.valueOf(((Attribute) attribute2).getValue()));
                }
            }else
                throw new NotImplementedException("");

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

        ret.setModel(model);
        return ret;
    }

    public static RDFModel fromJson(byte[] json){
        return fromJson(new String(json));
    }

    public static RDFModel fromJson(String jsonString){
        Model model = Utils.readModel(jsonString.getBytes());

        String uri = "";
        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()){
            Statement statement = iterator.next();
            if (statement.getPredicate().equals(RDF.type))
                uri = statement.getSubject().getURI();
        }
        return new RDFModel(uri, model);
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
        for(String nsKey: namespaces1.keySet())
            if(namespaces1.get(nsKey).equals(value))
                return nsKey;
        return null;
    }

    public EntityLD toEntityLD() throws Exception {

        String typeUri = getTypeURI();

        String typeNamespaceUri = model.createProperty(typeUri).getNameSpace();
        String type = (typeUri.startsWith(typeNamespaceUri)? typeUri.substring(typeNamespaceUri.length()) : Utils.getFragment(typeUri));
        String typeNsKey = null;
        if(typeNamespaceUri.endsWith(":")){ //ns is a prefix
            typeNsKey = typeNamespaceUri.substring(0, typeNamespaceUri.length()-1);
        }else {//ns is a full URI
            if (!namespaces1.containsValue(typeNamespaceUri)) {
                String[] splitted = URI.create(typeNamespaceUri).getPath().split("/");
                typeNsKey = splitted[splitted.length - 1];
                namespaces1.put(typeNsKey, typeNamespaceUri);
            }else
                typeNsKey = findNsByValue(typeNamespaceUri);
        }

        //String typePrefix = (typeNsKey!=null && !type.startsWith(typeNsKey+":")?typeNsKey+":":"");
        String typePrefix = typeNamespaceUri;

        StmtIterator iterator = this.model.listStatements();
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            if(!statement.getPredicate().equals(RDF.type)){

                Attribute attribute = null;

                String ns = statement.getPredicate().getNameSpace();
                String nsKey = null;
                String nsURI = null;
                if(ns.endsWith(":")){
                    nsKey = ns.substring(0, ns.length()-1);
                    if (!namespaces1.containsKey(nsKey))
                        throw new Exception("Namespace URI for "+nsKey+" not found");
//                        String[] splitted = URI.create(nsUri).getPath().split("/");
//                        namespaces.put(nsKey, splitted[splitted.length - 1]);
                    nsURI = namespaces1.get(nsKey);

                }else {
                    nsURI = ns;
                    if (!namespaces1.containsValue(nsURI)) {
                        String[] splitted = URI.create(nsURI).getPath().split("/");
                        nsKey = splitted[splitted.length - 1];
                        namespaces1.put(nsKey, nsURI);
                    }else
                        nsKey = findNsByValue(nsURI);

                }

                String localName = statement.getPredicate().getLocalName();
                String attrName = (nsKey!=null && !localName.startsWith(nsKey+":") ?nsKey+":":"")+localName;
                //String attrName = nsURI+localName;
                //String attrName = localName;

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
                     Object value =  attributes.get(attrName);
                     if (value instanceof List)
                         ((List)(value)).add(attribute);
                     else{
                         List list = new ArrayList();
                         list.add(value);
                         list.add(attribute);
                         LOGGER.warn("List insertion not implemented for attributes. Skipping {}", attrName);
                         //attributes.put(attrName, list);
                     }
                }else
                    attributes.put(attrName, attribute);
                //attributes.put(attrName, attValues);
            }
        }


        Map<String, Object> context = null;
        String jsonStr = toJsonLDString();
        JsonObject parsed = (JsonObject)new JsonParser().parse(jsonStr);
        if(parsed.get("@context") instanceof JsonObject){
            context = new Gson().fromJson(parsed.get("@context").toString(), HashMap.class);
            for(String NsKey: namespaces1.keySet())
            context.put(NsKey, namespaces1.get(NsKey));
        }


        EntityLD ret = new EntityLD(getURI(), typePrefix+type, attributes, context);
        return ret;
    }
}
