package com.agtinternational.iotcrawler.core;

import com.agtinternational.iotcrawler.core.commands.*;
import com.agtinternational.iotcrawler.core.models.IoTStream;
import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.google.gson.JsonObject;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandsTests {

    StreamObservation streamObservation;
    IoTStream ioTStream;


    @Before
    public void init(){
        streamObservation = new StreamObservation("iotc:Stream_Z-Wave+Node+003%3A+FGWP102+Meter+Living+Space_Electric+meter+%28watts%29");
        streamObservation.addProperty("iotc:state", 45);

        ioTStream = new IoTStream("http://purl.org/iot/ontology/iot-stream#Stream_FIBARO%2520Wall%2520plug%2520living%2520room_CurrentEnergyUse");
    }

    @Test
    public void GetEntityCommandTest() throws Exception {

        GetEntityCommand command = new GetEntityCommand(ioTStream.getURI(), 0);
        String jsonString = command.toJson();
        GetEntityCommand command2 = GetEntityCommand.fromJson(jsonString);

        org.junit.Assert.assertEquals(jsonString, command2.toJson());
    }

    @Test
    public void RegisterEntityCommandTest() throws Exception {


        RegisterEntityCommand command = new RegisterEntityCommand(ioTStream);
        String jsonString = command.toJson();
        RegisterEntityCommand pushObservationsCommand2 = RegisterEntityCommand.fromJson(jsonString);

        org.junit.Assert.assertEquals(jsonString, pushObservationsCommand2.toJson());
    }

    @Test
    public void PushObservationsCommandTest() throws Exception {

        List<StreamObservation> observationList = new ArrayList<>();
        observationList.add(streamObservation);

        PushObservationsCommand pushObservationsCommand = new PushObservationsCommand(observationList);
        String jsonString = pushObservationsCommand.toJson();
        PushObservationsCommand pushObservationsCommand2 = PushObservationsCommand.fromJson(jsonString);

        org.junit.Assert.assertEquals(jsonString, pushObservationsCommand2.toJson());
    }


    @Test
    public void GetObservationsCommandTest() throws Exception {

        GetObservationsCommand command = new GetObservationsCommand(streamObservation.getURI(), 0);
        String jsonString = command.toJson();
        GetObservationsCommand command2 = GetObservationsCommand.fromJson(jsonString);

        org.junit.Assert.assertEquals(jsonString, command2.toJson());
    }



    @Test
    public void SubscribeToEntityCommandTest() throws Exception {


        String[] attributes = new String[]{ "http://www.agtinternational.com/iotcrawler/ontologies/iotc#current_value" };
        NotifyCondition notifyCondition = new NotifyCondition(NotifyConditionEnum.ONCHANGE, Arrays.asList(attributes), null);
        SubscribeToEntityCommand command = new SubscribeToEntityCommand(
                ioTStream.getURI(),
                attributes,
                Arrays.asList(new NotifyCondition[]{ notifyCondition }),
                new Restriction());

        String jsonString = command.toJson();
        SubscribeToEntityCommand command2 = SubscribeToEntityCommand.fromJson(jsonString);

        org.junit.Assert.assertEquals(jsonString, command2.toJson());
    }



}
