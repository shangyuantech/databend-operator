package com.databend.operator.culster.crd;

import com.databend.operator.culster.crd.spec.meta.DatabendMeta;
import com.databend.operator.culster.crd.spec.DatabendQuery;
import com.databend.operator.culster.crd.spec.DatabendUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class DatabendClusterSpec {

    @JsonPropertyDescription("Databend users")
    private List<DatabendUser> users;

    private DatabendMeta meta;

    private DatabendQuery query;

    public List<DatabendUser> getUsers() {
        return users;
    }

    public void setUsers(List<DatabendUser> users) {
        this.users = users;
    }

    public DatabendMeta getMeta() {
        return meta;
    }

    public void setMeta(DatabendMeta meta) {
        this.meta = meta;
    }

    public DatabendQuery getQuery() {
        return query;
    }

    public void setQuery(DatabendQuery query) {
        this.query = query;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
