package com.agtinternational.iotcrawler.fiware.models;

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
                //new Object[]{ readEntity("samples/Vehicle.json") },
                //new Object[]{ readEntity("samples/TemperatureSensor.json") },
                new Object[]{ readEntity("samples/IoTStream.json") }
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

    @Test
    public void attributesToJson(){
        JsonObject jsonObject = Utils.mapToJson(entity.getAttributes());
        Assert.assertNotNull(jsonObject);
    }
}
