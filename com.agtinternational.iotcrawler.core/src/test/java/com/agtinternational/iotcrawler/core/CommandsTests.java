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
        String query = "sosa:observes.value=iotc:Property";
        GetEntitiesCommand command = new GetEntitiesCommand(ioTStream.getURI(),  query, null,0, 0);
        //GetEntitiesCommand command = new GetEntitiesCommand(IoTStream.class,  query, null,0, 0);
        String jsonString = command.toJson();
        //Class targetClass = Class.forName(command.getTargetClass());
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
