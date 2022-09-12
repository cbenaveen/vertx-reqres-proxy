package com.vertx.reqres_proxy;

import com.vertx.reqres_proxy.verticles.UserDataVerticle;
import com.vertx.reqres_proxy.verticles.WebServerVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusteredUserDetailsProxyApp {
    static {
        // set vertx logger delegate factory to slf4j
        String logFactory = System.getProperty("org.vertx.logger-delegate-factory-class-name");
        if (logFactory == null) {
            System.setProperty("org.vertx.logger-delegate-factory-class-name",
                    SLF4JLogDelegateFactory.class.getName());
        }
    }

    private static final Logger log = LoggerFactory.getLogger(ClusteredUserDetailsProxyApp.class);

    public static void main(String[] args) {
        // create vertx instance
        Vertx.clusteredVertx(new VertxOptions()).onSuccess(vertx -> {
            // create instance of config retriever to read the configuration from the
            // conf/config.json file
            ConfigRetriever.create(Vertx.vertx())
                    .getConfig()
                    // When successful deploy all the needed verticals.
                    .onSuccess(jsonObject -> {
                        log.info("The configuration JSON to be used is {}", jsonObject);

                        Future<String> webServerVerticleDeployFuture = Future.succeededFuture();

                        if (jsonObject.getBoolean("deployWebServer")) {
                            vertx.deployVerticle(WebServerVerticle.class.getName(),
                                    new DeploymentOptions().setConfig(jsonObject));
                        } else {
                            log.info("Skipping the web server verticle deployment as the configuration " +
                                    "`deployWebServer` set to false");
                        }

                        // deploy the User data verticle
                        Future<String> userDataVerticleDeployFuture = vertx.deployVerticle(UserDataVerticle.class.getName());

                        CompositeFuture.all(webServerVerticleDeployFuture, userDataVerticleDeployFuture)
                                .onSuccess(event -> {
                                    log.info("List of Futures that were deployed successful are: {}", event.list());
                                })
                                .onFailure(throwable -> log.error("One or more verticle deploy failed", throwable));

                        Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
                    })
                    .onFailure(throwable -> {
                        log.error("The configuration retrieval failed. Unable to deploy verticles", throwable);
                    });
        }).onFailure(throwable -> log.error("Unable to run the application in cluster mode", throwable));

    }
}
