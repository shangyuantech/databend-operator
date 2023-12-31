package com.databend.operator.culster.crd;

import com.databend.operator.common.type.StateType;
import com.databend.operator.culster.crd.status.MetaStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabendClusterStatus {

    private StateType state;

    private MetaStatus meta;

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public MetaStatus getMeta() {
        return meta;
    }

    public void setMeta(MetaStatus meta) {
        this.meta = meta;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
