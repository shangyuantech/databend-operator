package com.databend.operator.crd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Group("databend.datafuselabs.com")
@Kind("DatabendCluster")
@Plural("databendclusters")
@Singular("databendcluster")
@ShortNames({"dbc", "dbcs"})
@Version("v1")
public class DatabendCluster extends CustomResource<DatabendClusterSpec, DatabendClusterStatus> implements Namespaced {

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
