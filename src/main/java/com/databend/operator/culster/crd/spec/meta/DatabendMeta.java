package com.databend.operator.culster.crd.spec.meta;

import com.databend.operator.culster.crd.spec.DatabendImage;
import com.databend.operator.common.crd.Resources;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class DatabendMeta {

    private Integer size;

    private DatabendImage image;

    private String serviceAccount;

    private MetaService service;

    private Resources resources;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public DatabendImage getImage() {
        return image;
    }

    public void setImage(DatabendImage image) {
        this.image = image;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public MetaService getService() {
        return service;
    }

    public void setService(MetaService service) {
        this.service = service;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
