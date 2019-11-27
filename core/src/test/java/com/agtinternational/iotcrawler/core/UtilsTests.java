package com.agtinternational.iotcrawler.core;

import com.agtinternational.iotcrawler.core.models.RDFModel;
import org.junit.Test;

public class UtilsTests {

    @Test
    public void cutURLTest(){
        String key = Utils.cutURL("http://purl.org/iot/ontology/iot-stream#IoTStream", RDFModel.getNamespaces());
        String abc = "";

    }
}
