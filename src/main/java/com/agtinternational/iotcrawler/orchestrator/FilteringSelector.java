package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

public class FilteringSelector {

    private String subject;
    private String predicate;
    private String object;


    public FilteringSelector(){

    }

    public FilteringSelector(Builder builder){
        subject = builder.subject;
        predicate = builder.predicate;
        object = builder.object;

    }

    public String getObject() {
        return object;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getSubject() {
        return subject;
    }


    public void setObject(String value) {  object = value;    }

    public void setPredicate(String value) {
        predicate=value;
    }

    public void setSubject(String value) {
        subject=value;
    }

    public static class Builder{
        private String subject;
        private String predicate;
        private String object;
        private int limit;


        public Builder subject(String value){
            subject = value;
            return this;
        }

        public Builder predicate(String value){
            predicate = value;
            return this;
        }

        public Builder object(String value){
            object = value;
            return this;
        }

        public Builder limit(int value){
            limit = value;
            return this;
        }

        public FilteringSelector build(){
            return new FilteringSelector(this);
        }
    }
}
