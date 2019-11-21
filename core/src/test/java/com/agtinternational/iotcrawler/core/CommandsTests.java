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

import com.agtinternational.iotcrawler.core.commands.*;
import com.agtinternational.iotcrawler.core.models.IoTStream;
import com.agtinternational.iotcrawler.core.models.StreamObservation;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyConditionEnum;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;
import org.junit.Before;
import org.junit.Test;

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

        GetEntitiesCommand command = new GetEntitiesCommand(ioTStream.getURI(), 0, 0);
        String jsonString = command.toJson();
        GetEntitiesCommand command2 = GetEntitiesCommand.fromJson(jsonString);

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

        GetObservationsCommand command = new GetObservationsCommand(streamObservation.getURI(), 0,0);
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
