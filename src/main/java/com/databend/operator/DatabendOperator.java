package com.databend.operator;

import com.databend.operator.culster.DatabendClusterReconciler;
import io.javaoperatorsdk.operator.Operator;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

import java.io.IOException;

public class DatabendOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabendOperator.class);

    public static void main(String[] args) throws IOException, ParseException {
        // check args
        Options options = new Options();
        options.addOption("n", "namespace", true, "watch namespace");
        CommandLineParser cliParser = new DefaultParser();
        CommandLine cli = cliParser.parse(options, args);

        // watch namespaces
        String namespaces = cli.getOptionValue("n", "");
        if (StringUtils.isNotBlank(namespaces)) {
            LOGGER.info("watching namespaces are {}", namespaces);
        } else {
            LOGGER.info("watching all namespaces");
        }

        LOGGER.info("Starting Databend Operator ...");
        Operator operator = new Operator();
        LOGGER.info("Register Databend Cluster Reconciler ...");
        operator.register(new DatabendClusterReconciler(operator.getKubernetesClient()), configOverrider -> {
            if (StringUtils.isNotBlank(namespaces)) {
                configOverrider.settingNamespaces(namespaces.split(","));
            } else {
                configOverrider.watchingAllNamespaces();
            }
        });
        operator.start();
        LOGGER.info("Databend Operator started !");
        new FtBasic(new TkFork(new FkRegex("/health", "ALL GOOD.")), 8080).start(Exit.NEVER);
    }

}
