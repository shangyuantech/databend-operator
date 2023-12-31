package com.databend.operator;

import com.databend.operator.culster.DatabendClusterReconciler;
import io.javaoperatorsdk.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

import java.io.IOException;

public class DatabendOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabendOperator.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting Databend Operator ...");
        Operator operator = new Operator();
        LOGGER.info("Register Databend Cluster Reconciler ...");
        operator.register(new DatabendClusterReconciler(operator.getKubernetesClient()));
        operator.start();
        LOGGER.info("Databend Operator started !");
        new FtBasic(new TkFork(new FkRegex("/health", "ALL GOOD.")), 8080).start(Exit.NEVER);
    }

}
