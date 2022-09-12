package com.vertx.reqres_proxy;

import com.vertx.reqres_proxy.verticles.UserDataVerticle;
import com.vertx.reqres_proxy.verticles.WebServerVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Implementation that create the {@link Vertx} instances and deploy the {@link io.vertx.core.Verticle}.
 */
public class UserDetailsProxyApp {
    static {
        // set vertx logger delegate factory to slf4j
        String logFactory = System.getProperty("org.vertx.logger-delegate-factory-class-name");
        if (logFactory == null) {
            System.setProperty("org.vertx.logger-delegate-factory-class-name",
                    SLF4JLogDelegateFactory.class.getName());
        }
    }

    private static final Logger log = LoggerFactory.getLogger(UserDetailsProxyApp.class);

    public static void main(String[] args) {
        // create vertx instance
        Vertx vertx = Vertx.vertx();

        // create instance of config retriever to read the configuration from the
        // conf/config.json file
        ConfigRetriever.create(Vertx.vertx())
                .getConfig()
                // When successful deploy all the needed verticals.
                .onSuccess(jsonObject -> {
                    log.info("The configuration JSON to be used is {}", jsonObject);

                    Future<String> webServerVerticleDeployFuture = vertx.deployVerticle(WebServerVerticle.class.getName(),
                            new DeploymentOptions().setConfig(jsonObject));

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
    }
}
