package com.agtinternational.iotcrawler.fiware.models;

/*-
 * #%L
 * fiware-models
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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
                //new Object[]{ createEntity() },
                //new Object[]{ readEntity("samples/VehicleLD.json") },
                //new Object[]{ readEntity("samples/TemperatureSensorLD.json") },
                //new Object[]{ readEntity("samples/IoTStreamLD.json") }
                new Object[]{ readEntity("samples/PlatformLD.json") }
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
