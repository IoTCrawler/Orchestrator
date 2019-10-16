package com.agtinternational.iotcrawler.core;

import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class EnvVariablesSetter {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void init(){

        environmentVariables.set(Constants.IOTCRAWLER_RABBIT_HOST, "10.67.1.107");


    }
}
