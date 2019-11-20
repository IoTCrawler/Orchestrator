package com.agtinternational.iotcrawler.core.models;

import static com.agtinternational.iotcrawler.core.Constants.sosaPrefix;

public class SOSA {

    public static String madeBySensor = sosaPrefix+":madeBySensor";

    public static String hosts = sosaPrefix+":hosts";
    public static String isHostedBy = sosaPrefix+":isHostedBy";

    public static String observes = sosaPrefix+":observes";
    public static String isObservedBy = sosaPrefix+":isObservedBy";

    public static String observableProperty = sosaPrefix+":"+"ObservableProperty";

}
