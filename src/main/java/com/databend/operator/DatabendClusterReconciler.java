package com.databend.operator;

import com.databend.operator.crd.DatabendCluster;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.processing.event.source.EventSource;

import java.util.Map;

@ControllerConfiguration
public class DatabendClusterReconciler implements Reconciler<DatabendCluster>,
        Cleaner<DatabendCluster>, EventSourceInitializer<DatabendCluster> {

    @Override
    public UpdateControl<DatabendCluster> reconcile(DatabendCluster dc, Context<DatabendCluster> context) throws Exception {
        return null;
    }

    @Override
    public DeleteControl cleanup(DatabendCluster dc, Context<DatabendCluster> context) {
        return null;
    }

    @Override
    public Map<String, EventSource> prepareEventSources(EventSourceContext<DatabendCluster> esc) {
        return null;
    }
}
