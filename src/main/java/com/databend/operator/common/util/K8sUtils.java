package com.databend.operator.common.util;

import com.databend.operator.common.KubeConstants;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class K8sUtils {

    /**
     * create metadata
     */
    public static ObjectMeta createMetadata(String namespace, String name, Map<String, String> labels) {
        var builder = new ObjectMetaBuilder()
                .withName(name)
                .withLabels(labels);
        if (StringUtils.isNotBlank(namespace)) builder.withNamespace(namespace);
        return builder.build();
    }

    /**
     * get context labels
     */
    public static Map<String, String> getContextLabels(Context context) {
        return (Map<String, String>) context.managedDependentResourceContext()
                .getMandatory(KubeConstants.LABELS_CONTEXT_KEY, Map.class);
    }

    /**
     * check labels is same
     */
    public static boolean checkLabels(Map<String, String> left, Map<String, String> right) {
        int leftSize = left == null || left.isEmpty() ? 0 : left.size();
        int rightSize = right == null || right.isEmpty() ? 0 : right.size();
        if (leftSize != rightSize) return false;
        return Objects.equals(left, right);
    }

    /**
     * encode date
     */
    public static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * decode data
     */
    public static String decode(String value) {
        return new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }

    /**
     * read env to map
     */
    public static Map<String, String> convertEnvVar(List<EnvVar> envVars) {
        final var result = new HashMap<String, String>(envVars.size());
        envVars.forEach(e -> result.put(e.getName(), e.getValue()));
        return result;
    }

    /**
     * Get annotation value
     */
    public static String getAnnotation(HasMetadata metadata, String key, String defaultValue) {
        if (metadata.getMetadata().getAnnotations() == null) return defaultValue;
        return metadata.getMetadata().getAnnotations().getOrDefault(key, defaultValue);
    }
}
