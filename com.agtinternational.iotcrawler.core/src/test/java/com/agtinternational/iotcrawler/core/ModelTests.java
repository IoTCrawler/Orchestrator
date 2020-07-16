package com.agtinternational.iotcrawler.core;

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

import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextMetadata;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import org.apache.jena.atlas.lib.DateTimeUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ModelTests {
    //ToDo: test getProperty(URI)   getProperty(prefix+":"+name)

    @Test
    //JsonString->IoTStream->JsonString2->IoTStream2->JsonString3==JsonString2
    public void iotStreamConversionsTest() throws Exception {
        byte[] iotStreamJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamJson);

        String iotStreamJson2 = ioTStream.toJsonLDString();
        Files.write(Paths.get("target","IoTStream2.json"), iotStreamJson2.getBytes());

        IoTStream ioTStream2 = IoTStream.fromJson(iotStreamJson);

        String iotStreamJson3 = ioTStream2.toJsonLDString();
        Files.write(Paths.get("target","IoTStream3.json"), iotStreamJson3.getBytes());

        org.junit.Assert.assertEquals(iotStreamJson2, iotStreamJson3);

    }

    @Test
    //JsonString->IoTStream->JsonString2->IoTStream2->JsonString3==JsonString2
    public void iotStreamToEnitity() throws Exception {
        byte[] iotStreamJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamJson);
        ioTStream.addProperty("prop1","value1");
        ioTStream.addProperty("prop1","value2");
        ioTStream.setProperty("prop1","value3");

        EntityLD entityLD = ioTStream.toEntityLD();
        String iotStreamJson2 = ioTStream.toJsonLDString();
        Files.write(Paths.get("target","IoTStream2.json"), iotStreamJson2.getBytes());

        IoTStream ioTStream2 = IoTStream.fromJson(iotStreamJson);

        String iotStreamJson3 = ioTStream2.toJsonLDString();
        Files.write(Paths.get("target","IoTStream3.json"), iotStreamJson3.getBytes());

        org.junit.Assert.assertEquals(iotStreamJson2, iotStreamJson3);
    }



    @Test
    //IoTStream->EntityLD
    public void iotStreamFromEntityTest() throws Exception {

        //byte[] entityModelJson = Files.readAllBytes(Paths.get("samples/Entity.json"));
        String abc = IoTStream.getTypeUri(true);
        byte[] entityModelJson = Files.readAllBytes(Paths.get("samples/IoTStream.json"));
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityModelJson));
        entityLD.addAttribute(RDFS.label.getURI(), "Label1");
        RDFModel rdfModel1 = RDFModel.fromEntity(entityLD);
        IoTStream ioTStream = IoTStream.fromEntity(entityLD);

        JsonObject entityLDJson = entityLD.toJsonObject();

        String iotStreamJson = ioTStream.toJsonLDString();
        Files.write(Paths.get("target","IoTStream2.json"), iotStreamJson.getBytes());

        IoTStream ioTStream2 = IoTStream.fromJson(iotStreamJson);
        EntityLD entityLD2 = ioTStream2.toEntityLD();

        String entityLDJson2 = Utils.prettyPrint(entityLD2.toJsonObject());
        Files.write(Paths.get("target","Entity2.json"), entityLDJson2.getBytes());

        JsonObject entityLDJson3 = entityLD2.toJsonObject();
        org.junit.Assert.assertEquals(entityLDJson.toString(), entityLDJson3.toString());


    }

    @Test
    //JsonString->EntityLD->IoTStream->JsonString2->IoTStream2->EntityLD2==EntityLD
    public void streamObservationTest() throws Exception {
        byte[] json = Files.readAllBytes(Paths.get("samples/Observation3.json"));
        //StreamObservation streamObservation = StreamObservation.fromJson(json);

        StreamObservation streamObservation = new StreamObservation("iotc:Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%29");
        streamObservation.madeBySensor("iotc:Sensor_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Sensor+%28power%29");
        streamObservation.hasResult(45);
        streamObservation.resultTime(DateTimeUtils.nowAsString());

        String jsonLDString = streamObservation.toJsonLDString();
        StreamObservation streamObservation2 = StreamObservation.fromJson(jsonLDString);
        org.junit.Assert.assertEquals(jsonLDString, streamObservation2.toJsonLDString());

    }

    @Test
    //JsonString->EntityLD->IoTStream->JsonString2->IoTStream2->EntityLD2==EntityLD
    public void streamObservationFromBrokerTest() throws Exception {

        EntityId entity = new EntityId("iotc:Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%29", URI.create("iotc:StreamObservation"), false);

        ContextMetadata contextMetadata = new ContextMetadata();
        contextMetadata.setType(URI.create(XSDDatatype.XSDlong.getURI()));
        List<ContextMetadata> metadata = null; //new ArrayList<ContextMetadata>(){{ add(contextMetadata); }};
        ContextAttribute lightLevel = new ContextAttribute("http://www.agtinternational.com/ontologies/SmartHome#current_value", URI.create(XSDDatatype.XSDlong.getURI()), String.valueOf(System.currentTimeMillis()), metadata);
        List<ContextAttribute> contextAttributeList = new ArrayList<ContextAttribute>(){{ add(lightLevel); }};

        ContextElement ret = new ContextElement();
        ret.setEntityId(new EntityId("lightSensor", URI.create("LightSensor"), false));
        ret.setContextAttributeList(new ArrayList<ContextAttribute>(){{ add(lightLevel); }});
        ret.setDomainMetadata(new ArrayList<ContextMetadata>());


        ContextElement contextElement = new ContextElement();
        contextElement.setEntityId(entity);
        contextElement.setContextAttributeList(contextAttributeList);

        StreamObservation streamObservation = StreamObservation.fromContextElement(contextElement);
        String jsonLDString = streamObservation.toJsonLDString();

        //org.junit.Assert.assertEquals(jsonLDString, streamObservation2.toJsonLDString());

    }

    @Test
    @Ignore
    public void test1() throws Exception {
        //byte[] iotStreamJson = Files.readAllBytes(Paths.get("samples/IoTStream2.json"));
        //IoTStream ioTStream = IoTStream.fromJson(iotStreamJson);

        ObservableProperty observableProperty = new ObservableProperty("http://property1", "Property1");

        IoTStream ioTStream = new IoTStream("http://stream1", "Stream1");
        String abc = ioTStream.toJsonLDString();
        EntityLD entityLD = ioTStream.toEntityLD();
        EntityLD entityWithShortUris = ioTStream.toEntityLD(true);

        String label = ioTStream.label();
        Object rdfNode = ioTStream.getProperty("sosa:madeBySensor");
        Object rdfNode2 = ioTStream.getProperty("madeBySensor");
        //String sensorsUri = ioTStream.getSensorUri();//getSensor();



    }

    @Test
    @Ignore
    public void test2() throws Exception {

        byte[] json = Files.readAllBytes(Paths.get("samples/Platform.json"));
        Platform sosaPlatform = Platform.fromJson(json);

        //RDFModel[] array = new RDFModel[]{ new RDFModel("http://sensor1"), new RDFModel("http://sensor2") } ;
        //sosaPlatform.hosts(array);

        JsonObject jsonObject1 = sosaPlatform.toJsonObject();
        System.out.println(Utils.prettyPrint(jsonObject1));

        System.out.println("--------------------------------------");

        EntityLD entityLD = sosaPlatform.toEntityLD();
        JsonObject jsonObject2 = entityLD.toJsonObject();
        System.out.println(Utils.prettyPrint(jsonObject2));

        Platform platform = Platform.fromEntity(entityLD);

        //Assert.assertEquals(jsonObject1, jsonObject2);
        String abc = "abc";
    }

    @Test
    @Ignore
    public void test3() throws Exception {

        byte[] json = Files.readAllBytes(Paths.get("samples/Platform.json"));
        Platform sosaPlatform = Platform.fromJson(json);

        RDFModel[] array = new RDFModel[]{ new RDFModel("http://sensor1"), new RDFModel("http://sensor2") } ;
        sosaPlatform.hosts(array);

        JsonObject jsonObject1 = sosaPlatform.toJsonObject();
        System.out.println(Utils.prettyPrint(jsonObject1));

        System.out.println("--------------------------------------");

        EntityLD entityLD = sosaPlatform.toEntityLD(true);
        JsonObject jsonObject2 = entityLD.toJsonObject();
        System.out.println(Utils.prettyPrint(jsonObject2));

        //Assert.assertEquals(jsonObject1, jsonObject2);
        String abc = "abc";
    }


}
