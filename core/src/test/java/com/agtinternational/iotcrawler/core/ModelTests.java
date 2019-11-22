package com.agtinternational.iotcrawler.core;

/*-
 * #%L
 * core
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

import com.agtinternational.iotcrawler.core.models.IoTStream;
import com.agtinternational.iotcrawler.core.models.Platform;
import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextMetadata;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.junit.Ignore;
import org.junit.Test;

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

        byte[] entityModelJson = Files.readAllBytes(Paths.get("samples/EntityFromBroker.json"));
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityModelJson));

        IoTStream ioTStream = IoTStream.fromEntity(entityLD);

        String iotStreamJson = ioTStream.toJsonLDString();
        Files.write(Paths.get("target","IoTStream2.json"), iotStreamJson.getBytes());

        IoTStream ioTStream2 = IoTStream.fromJson(iotStreamJson);
        EntityLD entityLD2 = ioTStream2.toEntityLD();

        String entityLDJson2 = Utils.prettyPrint(entityLD2.toJsonObject());
        Files.write(Paths.get("target","Entity2.json"), entityLDJson2.getBytes());

        org.junit.Assert.assertEquals(entityLD.toJsonObject().toString(), entityLD2.toJsonObject().toString());


    }

    @Test
    //JsonString->EntityLD->IoTStream->JsonString2->IoTStream2->EntityLD2==EntityLD
    public void streamObservationTest() throws Exception {
        byte[] json = Files.readAllBytes(Paths.get("samples/Observation3.json"));
        //StreamObservation streamObservation = StreamObservation.fromJson(json);

        StreamObservation streamObservation = new StreamObservation("iotc:Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%29");
        streamObservation.addProperty("iotc:state", 45);

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
        byte[] iotStreamJson = Files.readAllBytes(Paths.get("samples/IoTStream2.json"));
        IoTStream ioTStream = IoTStream.fromJson(iotStreamJson);

        byte[] json = Files.readAllBytes(Paths.get("samples/Platform.json"));
        Platform sosaPlatform = Platform.fromJson(json);

        Object hosts = sosaPlatform.hosts();
        String label = ioTStream.getLabel();
        Object rdfNode = ioTStream.getProperty("sosa:madeBySensor");
        Object rdfNode2 = ioTStream.getProperty("madeBySensor");
        //String sensorsUri = ioTStream.getSensorUri();//getSensor();
        String abc = "123";
    }




}
