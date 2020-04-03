package com.agtinternational.iotcrawler.fiware.models.subscription;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.ngsi2.model.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscription {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<EntityInfo> entities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> watchedAttributes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    NotificationParams notification;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Instant expires;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    com.orange.ngsi2.model.Subscription.Status status;



    @JsonInclude(JsonInclude.Include.NON_NULL)
    Number throttling;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("q")
    Map<String, String> query;

    public Subscription(String id, List<EntityInfo> entities, List<String> watchedAttributes, NotificationParams notification, Instant expires, Number throttling){
        this.id = id;
        this.entities = entities;
        this.watchedAttributes = watchedAttributes;

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

    public com.orange.ngsi2.model.Subscription.Status getStatus() {
        return this.status;
    }

    public void setStatus(com.orange.ngsi2.model.Subscription.Status status) {
        this.status = status;
    }

    public Instant getExpires() {
        return this.expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public void setEntities(List<EntityInfo> entities) {
        this.entities = entities;
    }

    public List<EntityInfo> getEntities() {
        return entities;
    }

    @JsonIgnore
    public void setQuery(String key, String value) {
        if (this.query == null) {
            this.query = new HashMap();
        }

        ((Map)this.query).put(key, value);
    }


    public static enum Status {
        active,
        expired;

        private Status() {
        }
    }
}
