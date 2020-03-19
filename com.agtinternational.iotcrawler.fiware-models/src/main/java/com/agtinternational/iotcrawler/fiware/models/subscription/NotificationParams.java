package com.agtinternational.iotcrawler.fiware.models.subscription;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NotificationParams /*extends Notification*/ {
    List<String> attributes;
    Endpoint endpoint;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    Optional<Map<String, String>> headers;
//    @JsonInclude(JsonInclude.Include.NON_ABSENT)
//    Optional<Map<String, String>> query;
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    Optional<com.orange.ngsi2.model.Notification.Format> attrsFormat;


    long timesSent;
    Instant lastNotification;

    public NotificationParams() {
    }

    public NotificationParams(List<String> attributes, Endpoint endpoint) {
        this.attributes = attributes;
        this.endpoint = endpoint;
    }

    public List<String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public Endpoint getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

//    public Optional<Long> getThrottling() {
//        return this.throttling;
//    }
//
//    public void setThrottling(Optional<Long> throttling) {
//        this.throttling = throttling;
//    }
//
//    public long getTimesSent() {
//        return this.timesSent;
//    }
//
//    public void setTimesSent(long timesSent) {
//        this.timesSent = timesSent;
//    }
//
//    public Instant getLastNotification() {
//        return this.lastNotification;
//    }
//
//    public void setLastNotification(Instant lastNotification) {
//        this.lastNotification = lastNotification;
//    }
//
//    public Optional<Map<String, String>> getHeaders() {
//        return this.headers;
//    }
//
//    public void setHeaders(Optional<Map<String, String>> headers) {
//        this.headers = headers;
//    }
//
//    @JsonIgnore
//    public void setHeader(String key, String value) {
//        if (this.headers == null) {
//            this.headers = Optional.of(new HashMap());
//        }
//
//        ((Map)this.headers.get()).put(key, value);
//    }




    public Optional<com.orange.ngsi2.model.Notification.Format> getAttrsFormat() {
        return this.attrsFormat;
    }

    public void setAttrsFormat(Optional<com.orange.ngsi2.model.Notification.Format> attrsFormat) {
        this.attrsFormat = attrsFormat;
    }

    public static enum Format {
        normalized,
        keyValues,
        values;

        private Format() {
        }
    }
}
