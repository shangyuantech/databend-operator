package com.databend.operator.culster;

import com.databend.operator.common.util.JsonUtils;
import com.databend.operator.culster.crd.DatabendCluster;
import com.databend.operator.culster.dependent.MetaServiceResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.databend.operator.common.KubeConstants.*;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = MetaServiceResource.class, name = "meta-service"),
        })
public class DatabendClusterReconciler implements Reconciler<DatabendCluster>,
        ContextInitializer<DatabendCluster>{

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabendClusterReconciler.class);

    private final KubernetesClient kubernetesClient;

    public DatabendClusterReconciler(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void initContext(DatabendCluster cluster, Context<DatabendCluster> context) {
        // Add some common labels to resources
        final var name = cluster.getMetadata().getName();
        final var labels = Map.of(APP_LABEL, name,
                INSTANCE_LABEL, DATABEND_CLUSTER_INSTANCE_NAME,
                VERSION_LABEL, DEFAULT_APPLICATION_VERSION,
                COMPONENT_LABEL, OPERATOR_NAME);
        context.managedDependentResourceContext().put(LABELS_CONTEXT_KEY, labels);
    }

    @Override
    public UpdateControl<DatabendCluster> reconcile(DatabendCluster cluster, Context<DatabendCluster> context) throws Exception {
        final var spec = cluster.getSpec();
        final var namespace = cluster.getMetadata().getNamespace();
        final var name = cluster.getMetadata().getName();
        LOGGER.info("Register Databend Cluster {}/{} with spec: {}", namespace, name, JsonUtils.toJson(spec));

        // create/update databend-meta
        return UpdateControl.noUpdate();
    }

//    @Override
//    public DeleteControl cleanup(DatabendCluster dc, Context<DatabendCluster> context) {
//        return DeleteControl.defaultDelete();
//    }
}
