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

import com.google.gson.JsonObject;
import com.orange.ngsi2.model.Attribute;
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
                //new Object[]{ createEntity() },
                //new Object[]{ readEntity("samples/VehicleLD.json") },
                //new Object[]{ readEntity("samples/TemperatureSensorLD.json") },
                //new Object[]{ readEntity("samples/IoTStreamLD.json") }
                new Object[]{ readEntity("samples/PlatformLD.json") }
                //new Object[]{ readEntity("samples/MultiplePropertyValuesEntity.json") }

        });
    }

    public ModelsTest(EntityLD entityLD){
        this.entity = entityLD;
    }

    private static EntityLD readEntity(String path) throws Exception {

        byte[] entityJson = Files.readAllBytes(Paths.get(path));
        EntityLD entityLD = EntityLD.fromJsonString(new String(entityJson));
        return entityLD;
    }

    @Test
    //JsonString->EntityLD->JsonString2->Entity2==Entity
    public void fromJsonStringTest() throws Exception {
        Attribute attribute = entity.getAttribute("sosa:hosts");
        Attribute attribute2 = entity.getAttribute("sosa:hosts");
        Attribute attribute3 = entity.getAttribute("sosa:hosts");
        String JsonString2 = Utils.prettyPrint(entity.toJsonObject());
        Files.write(Paths.get("target",Utils.getFragment(entity.getId()+".json")), JsonString2.getBytes());

        EntityLD entityLD2 = EntityLD.fromJsonString(JsonString2);

        Assert.assertEquals(entityLD2.toJsonObject(), entity.toJsonObject());
    }

//    @Test
//    public void attributesToJson(){
//        JsonObject jsonObject = Utils.mapToJson(entity.getAttributes());
//        Assert.assertNotNull(jsonObject);
//    }
}
