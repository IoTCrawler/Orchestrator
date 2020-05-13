package com.agtinternational.iotcrawler.fiware.models;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.fiware.models.NGSILD.Relationship;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ModelsTest {

    private final EntityLD entity;

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][]{
                new Object[]{ createEntity() },
                new Object[]{ readEntityFromFile("samples/VehicleLD.json") },
                new Object[]{ readEntityFromFile("samples/urn-ngsi-ld-TemperatureSensor-335547902.json") },
                new Object[]{ readEntityFromFile("samples/urn-ngsi-ld-Sensor_AEON_Labs_ZW100_MultiSensor_6_BatteryLevel.json") },
                new Object[]{ readEntityFromFile("samples/IoTStreamLD.json") },
                new Object[]{ readEntityFromFile("samples/PlatformLD.json") },
                new Object[]{ readEntityFromFile("samples/MultiplePropertyValuesEntity.json") }

        });
    }

    public ModelsTest(EntityLD entityLD){
        this.entity = entityLD;
    }

    private static EntityLD readEntityFromFile(String path) throws Exception {

        byte[] entityJson = Files.readAllBytes(Paths.get(path));
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityJson));
        return entityLD;
    }

    private static EntityLD createEntity() throws IOException {
        EntityLD ret = new EntityLD("Entity1","sosa:Sensor");
        ret.addAttribute("hosts", new Relationship("value1"));
//        entityLD.addAttribute("hosts", new Relationship("value2"));
//        entityLD.addAttribute("hosts", new Relationship("value3"));
//        entityLD.addAttribute("hosts", new Property("value4"));
        return ret;
    }

    @Test
    public void fromJsonStringTest() throws Exception {
        Object attribute = entity.getAttribute("hosts");
        String JsonString2 = Utils.prettyPrint(entity.toJsonObject());
        System.out.println(JsonString2);
        //Files.write(Paths.get("target",Utils.getFragment(entity.getId()+".json")), JsonString2.getBytes());

//        EntityLD entityLD2 = EntityLD.fromJsonString(JsonString2);
//        String jsonString3 = Utils.prettyPrint(entityLD2.toJsonObject());
//        System.out.println(JsonString2);
//        Assert.assertEquals(entityLD2.toJsonObject(), entity.toJsonObject());
    }



    @Test
    public void createModelFromCode() throws Exception {

        JsonObject jsonObject = entity.toJsonObject();
        String jsonString = Utils.prettyPrint(jsonObject);

        if(!Files.exists(Paths.get("target")))
            Files.createDirectory(Paths.get("target"));

        if(!Files.exists(Paths.get("target","samples")))
            Files.createDirectory(Paths.get("target","samples"));

        Files.write(Paths.get("target","samples",Utils.getFragment(entity.getId().replace(":","-")+".json")), jsonString.getBytes());

        System.out.println(jsonString);
    }

//    @Test
//    public void attributesToJson(){
//        JsonObject jsonObject = Utils.mapToJson(entity.getAttributes());
//        Assert.assertNotNull(jsonObject);
//    }
}
