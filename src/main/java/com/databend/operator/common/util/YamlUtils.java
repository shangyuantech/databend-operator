package com.databend.operator.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.fabric8.kubernetes.client.utils.Serialization;

public class YamlUtils {

    public static String toPrettyYaml(Object pojoObject) {
        try {
            return YAML_MAPPER.writeValueAsString(pojoObject);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Read yaml failed！" + pojoObject.getClass().getName(), ex);
        }
    }

    private static final YAMLMapper YAML_MAPPER = YAMLMapper.builder(new YAMLFactory()
                    .disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID))
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .build();

    static {
        YAML_MAPPER.registerModules(Serialization.UNMATCHED_FIELD_TYPE_MODULE);
    }

}
