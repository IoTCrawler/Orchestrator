package com.agtinternational.iotcrawler.fiware.models.subscription;

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object[] data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String notifiedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String subscriptionId;

    public Notification(){

    }

    public String getType() {
        return type;
    }


    public Object[] getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getNotifiedAt() {
        return notifiedAt;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNotifiedAt(String notifiedAt) {
        this.notifiedAt = notifiedAt;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public static Notification fromJsonString(String jsonString) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Notification ret = mapper.readValue(jsonString, Notification.class);

        return ret;
    }
}
