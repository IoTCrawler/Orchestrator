package com.agtinternational.iotcrawler.fiware.models.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.ngsi2.model.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SubscriptionLD  {
    String id;

    NotificationParams notification;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Instant expires;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Subscription.Status status;
    List<SubjectEntity> entities;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    Number throttling;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @JsonProperty("q")
    Optional<Map<String, String>> query;

    public SubscriptionLD(String id, List<SubjectEntity> entities, NotificationParams notification, Instant expires, Number throttling){
        this.id = id;

        this.notification = notification;
        this.expires = expires;
        this.entities = entities;
        this.throttling = throttling;

//        if(subject!=null) {
//            entities = subject.getEntities();
//            condition = subject.getCondition();
//        }

    }

    public String getType() {
        return "Subscription";
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getThrottling() {
        return this.throttling;
    }

    public void setThrottling(Number throttling) {
        this.throttling = throttling;
    }

    public NotificationParams getNotification() {
        return this.notification;
    }

    public void setNotification(NotificationParams notification) {
        this.notification = notification;
    }

    public Subscription.Status getStatus() {
        return this.status;
    }

    public void setStatus(Subscription.Status status) {
        this.status = status;
    }

    public Instant getExpires() {
        return this.expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public void setQuery(Optional<Map<String, String>> query) {
        this.query = query;
    }

    @JsonIgnore
    public void setQuery(String key, String value) {
        if (this.query == null) {
            this.query = Optional.of(new HashMap());
        }

        ((Map)this.query.get()).put(key, value);
    }


    public static enum Status {
        active,
        expired;

        private Status() {
        }
    }
}
