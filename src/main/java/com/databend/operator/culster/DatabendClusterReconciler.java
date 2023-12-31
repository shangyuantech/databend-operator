package com.databend.operator.culster;

import com.databend.operator.common.KubeConstants;
import com.databend.operator.common.type.StateType;
import com.databend.operator.common.util.JsonUtils;
import com.databend.operator.common.util.K8sUtils;
import com.databend.operator.culster.crd.DatabendCluster;
import com.databend.operator.culster.crd.DatabendClusterStatus;
import com.databend.operator.culster.crd.status.MetaStatus;
import com.databend.operator.culster.dependent.MetaServiceResource;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.databend.operator.common.KubeConstants.*;

@ControllerConfiguration(
        dependents = {
                @Dependent(type = MetaServiceResource.class),
        })
public class DatabendClusterReconciler implements Reconciler<DatabendCluster>,
        ContextInitializer<DatabendCluster>,
        Cleaner<DatabendCluster> {

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

        // Create/update databend-meta
        String metaName = name + "-databend-meta";
        StatefulSet meta = kubernetesClient.apps().statefulSets().inNamespace(name).withName(metaName).get();
        // There are two states: bootstrap and completed bootstrap.
        // We need to switch different commands according to different states.
        // After the first bootstrap success, the command will be reset
        if (meta == null) {
            LOGGER.info("Creating Databend Meta {}/{} with boostrap", namespace, metaName);
            kubernetesClient.resource(createMeta(cluster, metaName, true)).create();
        } else {
            // Check whether the bootstrap has been completed. If yes, handle it as normal starting
            boolean boostrap = Boolean.getBoolean(K8sUtils.getAnnotation(meta, META_BOOSTRAP, "true"));
            if (boostrap) {
                // Check the current pod status
                // If in bootstrapping, we need to wait for the pod to complete boostrap before continuing the process
            } else {
                kubernetesClient.resource(createMeta(cluster, metaName, false)).create();
            }
        }

        // Create a listener to watch statefulset pods status,
        // periodically connect to the meta and check the current status of the meta.
        // In this way, we can monitor meta's health through this listener

        // Update status
        DatabendClusterStatus status = new DatabendClusterStatus();
        MetaStatus metaStatus = new MetaStatus();
        metaStatus.setState(StateType.ready);
        status.setState(StateType.ready);
        status.setMeta(metaStatus);
        cluster.setStatus(status);

        return UpdateControl.updateStatus(cluster);
    }

    /**
     * Create meta statefulset
     */
    private StatefulSet createMeta(DatabendCluster cluster, String name, boolean bootstrap) {
        StatefulSetBuilder builder = new StatefulSetBuilder();

        return builder.build();
    }

    @Override
    public DeleteControl cleanup(DatabendCluster cluster, Context<DatabendCluster> context) {
        final var namespace = cluster.getMetadata().getNamespace();
        final var name = cluster.getMetadata().getName();
        LOGGER.info("Delete Databend Cluster {}/{} ", namespace, name);
        return DeleteControl.defaultDelete();
    }
}
