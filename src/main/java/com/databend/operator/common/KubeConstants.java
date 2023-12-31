package com.databend.operator.common;

public interface KubeConstants {

    /* labels refer https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/ */
    String APP_LABEL = "app.kubernetes.io/name";
    String INSTANCE_LABEL = "app.kubernetes.io/instance";
    String VERSION_LABEL = "app.kubernetes.io/version";
    String COMPONENT_LABEL = "app.kubernetes.io/component";
    String PART_OF_LABEL = "app.kubernetes.io/part-of";
    String MANAGED_BY_LABEL = "app.kubernetes.io/managed-by";
    String CREATED_BY_LABEL = "app.kubernetes.io/created-by";

    String LABELS_CONTEXT_KEY = "labels";

    String OPERATOR_NAME = "databend-operator";
    String DATABEND_CLUSTER_INSTANCE_NAME = "databend-cluster";

    String DEFAULT_APPLICATION_VERSION = "1.0.0";
    String META_BOOSTRAP = "databend/meta-boostrap";

    String ClusterIP = "ClusterIP";
}
