package com.agtinternational.iotcrawler.fiware.clients;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.Duration;

import eu.neclab.iotplatform.ngsi.api.datamodel.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(
        name = "subscribeContextRequest"
)
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomSubscribeContextRequest extends SubscribeContextRequest {

    @Override
    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config1 = mapper.getSerializationConfig();
        config1.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        String jsonString = "";

        SubscribeContextRequest2 request2 = new SubscribeContextRequest2(this);
        try {
            jsonString = mapper.writeValueAsString(request2);
        } catch (JsonGenerationException var5) {
            var5.printStackTrace();
        } catch (JsonMappingException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return jsonString;
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class SubscribeContextRequest2 extends NgsiStructure {
    @XmlElementWrapper(
            name = "entityIdList"
    )
    @XmlElement(
            name = "entityId",
            required = true
    )
    @JsonProperty("entities")
    private List<EntityId> entityId;
    @XmlElementWrapper(
            name = "attributeList"
    )
    @XmlElement(
            name = "attribute",
            nillable = true
    )
    @JsonProperty("attributes")
    protected List<String> attribute;
    @XmlElement(
            name = "reference",
            required = true
    )
    private String reference = null;
    @XmlElement(
            name = "duration"
    )
    private Duration duration = null;
    @XmlElement(
            name = "restriction"
    )
    private Restriction restriction = null;
    @XmlElementWrapper(
            name = "notifyConditions"
    )
    @XmlElement(
            name = "notifyConditions"
    )
    @JsonProperty("notifyConditions")
    private List<NotifyCondition> notifyConditions;
    @XmlElement(
            name = "throttling"
    )
    private Duration throttling = null;

    public SubscribeContextRequest2(SubscribeContextRequest original){
        setEntityIdList(original.getEntityIdList());
        setDuration(original.getDuration());
        setAttributeList(original.getAttributeList());
        setNotifyConditions(original.getNotifyCondition());
        setReference(original.getReference());
        setRestriction(original.getRestriction());
        setThrottling(original.getThrottling());
    }

    @JsonIgnore
    public List<EntityId> getEntityIdList() {
        if (this.entityId == null) {
            this.entityId = new ArrayList();
        }

        return this.entityId;
    }

    @JsonIgnore
    public void setEntityIdList(List<EntityId> entityId) {
        this.entityId = entityId;
    }

    @JsonIgnore
    public List<EntityId> getAllEntity() {
        return new ArrayList(this.entityId);
    }

    @JsonIgnore
    public List<String> getAttributeList() {
        if (this.attribute == null) {
            this.attribute = new ArrayList();
        }

        return this.attribute;
    }

    @JsonIgnore
    public void setAttributeList(List<String> attributeList) {
        this.attribute = attributeList;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String ref) {
        this.reference = ref;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Restriction getRestriction() {
        return this.restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public List<NotifyCondition> getNotifyConditions() {
        if (this.notifyConditions == null) {
            this.notifyConditions = new ArrayList();
        }

        return this.notifyConditions;
    }

    public void setNotifyConditions(List<NotifyCondition> notifyConditions) {
        this.notifyConditions = notifyConditions;
    }

    public Duration getThrottling() {
        return this.throttling;
    }

    public void setThrottling(Duration throttling) {
        this.throttling = throttling;
    }
}
