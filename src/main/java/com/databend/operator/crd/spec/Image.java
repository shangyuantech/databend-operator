package com.databend.operator.crd.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    private String registry;
    private String repository;
    private String pullPolicy;
    private String tag;
    private List<String> imagePullSecrets;

}
