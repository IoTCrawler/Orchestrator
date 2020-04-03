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

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NotificationParams /*extends Notification*/ {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> attributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Endpoint endpoint;
    //@JsonInclude(JsonInclude.Include.NON_ABSENT)
    //Optional<Map<String, String>> headers;
//    @JsonInclude(JsonInclude.Include.NON_ABSENT)
//    Optional<Map<String, String>> query;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String format;


    long timesSent;
    Instant lastNotification;

    public NotificationParams() {
    }

    public NotificationParams(List<String> attributes, String format, Endpoint endpoint) {
        this.attributes = attributes;
        this.format = format;
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




    public String getFormat() {
        return this.format;
    }

    public void setAttrsFormat(String attrsFormat) {
        this.format = attrsFormat;
    }

    public static enum Format {
        normalized,
        keyValues,
        values;

        private Format() {
        }
    }
}
