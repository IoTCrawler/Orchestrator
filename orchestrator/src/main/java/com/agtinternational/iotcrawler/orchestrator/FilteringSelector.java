package com.agtinternational.iotcrawler.orchestrator;

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
