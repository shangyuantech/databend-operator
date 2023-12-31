package com.databend.operator.culster.dependent;

import com.databend.operator.common.util.K8sUtils;
import com.databend.operator.common.util.YamlUtils;
import com.databend.operator.culster.crd.DatabendCluster;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.Creator;
import io.javaoperatorsdk.operator.processing.dependent.Matcher;
import io.javaoperatorsdk.operator.processing.dependent.Updater;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependentResource;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.databend.operator.common.KubeConstants.ClusterIP;

public class MetaServiceResource extends KubernetesDependentResource<Service, DatabendCluster> implements
        Creator<Service, DatabendCluster>,
        Updater<Service, DatabendCluster>,
        Matcher<Service, DatabendCluster> {

    public static final Integer DEFAULT_ADMIN_PORT = 28002;
    public static final Integer DEFAULT_GRPC_PORT = 9191;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaServiceResource.class);

    public MetaServiceResource() {
        super(Service.class);
    }

    /**
     * Get service config
     *
     * @return [adminPort, grpcPort, serviceType]
     */
    private Tuple3<Integer, Integer, String> getServiceConfig(DatabendCluster cluster) {
        Integer adminPort = DEFAULT_ADMIN_PORT;
        Integer grpcPort = DEFAULT_GRPC_PORT;
        String serviceType = ClusterIP;
        var meta = cluster.getSpec().getMeta();
        if (meta != null && meta.getService() != null) {
            var serviceDesc = meta.getService();
            if (StringUtils.isNotBlank(serviceDesc.getServiceType())) serviceType = serviceDesc.getServiceType();
            var ports = serviceDesc.getPorts();
            if (ports != null) {
                if (ports.getAdmin() != null) adminPort = ports.getAdmin();
                if (ports.getGrpc() != null) grpcPort = ports.getGrpc();
            }
        }
        return new Tuple3<>(adminPort, grpcPort, serviceType);
    }

    @Override
    protected Service desired(DatabendCluster cluster, Context<DatabendCluster> context) {
        final var namespace = cluster.getMetadata().getNamespace();
        final var name = cluster.getMetadata().getName();
        final var labels = K8sUtils.getContextLabels(context);

        Tuple3<Integer, Integer, String> serviceConfig = getServiceConfig(cluster);
        Service service = new ServiceBuilder()
                .withMetadata(K8sUtils.createMetadata(namespace, String.format("%s-databend-meta", name), labels))
                .withNewSpec()
                .withSelector(labels)
                .withType(serviceConfig.v3)
                .withClusterIP("None")
                .withPublishNotReadyAddresses(true)
                // admin
                .addNewPort()
                .withName("admin")
                .withPort(serviceConfig.v1)
                .withNewTargetPort().withValue("admin").endTargetPort()
                .endPort()
                // grpc
                .addNewPort()
                .withName("grpc")
                .withPort(serviceConfig.v2)
                .withNewTargetPort().withValue("grpc").endTargetPort()
                .endPort()
                .endSpec()
                .build();

        LOGGER.info("Create/Replace service {}/{}", namespace, name);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("show {} yaml = \n{}", Service.class.getName(), YamlUtils.toPrettyYaml(service));
        }
        return service;
    }

    @Override
    public Result<Service> match(Service actual, DatabendCluster primary, Context<DatabendCluster> context) {
        final var ports = actual.getSpec().getPorts();
        Tuple3<Integer, Integer, String> serviceConfig = getServiceConfig(primary);
        // check port is same
        boolean match = true;
        for (ServicePort port : ports) {
            if ("admin".equals(port.getName())) {
                if (!Objects.equals(port.getPort(), serviceConfig.v1) || !"admin".equals(port.getTargetPort().getStrVal())) {
                    match = false;
                    break;
                }
            } else if ("grpc".equals(port.getName())) {
                if (!Objects.equals(port.getPort(), serviceConfig.v2) || !"grpc".equals(port.getTargetPort().getStrVal())) {
                    match = false;
                    break;
                }
            } else {
                match = false;
            }
        }
        if (!actual.getSpec().getType().equals(serviceConfig.v3)) match = false;
        if (!actual.getSpec().getClusterIP().equals("None")) match = false;
        if (!match) LOGGER.info("Current Service has changed, need to upgrade!");
        return Result.nonComputed(match);
    }
}
