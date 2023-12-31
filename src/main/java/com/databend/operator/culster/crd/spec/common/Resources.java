package com.databend.operator.culster.crd.spec.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.fabric8.kubernetes.api.model.ResourceRequirements;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class Resources {

    private ResourceRequirements requests;

    private ResourceRequirements limits;

    public ResourceRequirements getRequests() {
        return requests;
    }

    public void setRequests(ResourceRequirements requests) {
        this.requests = requests;
    }

    public ResourceRequirements getLimits() {
        return limits;
    }

    public void setLimits(ResourceRequirements limits) {
        this.limits = limits;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
