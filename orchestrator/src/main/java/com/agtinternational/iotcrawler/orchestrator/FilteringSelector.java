package com.agtinternational.iotcrawler.orchestrator;

/*-
 * #%L
 * orchestrator
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
