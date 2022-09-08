package com.vertx.reqres_proxy;

import com.vertx.reqres_proxy.verticles.UserDataVerticle;
import com.vertx.reqres_proxy.verticles.WebServerVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class UserDetailsProxyApp {
  public static void main(String[] args) {
    // create vertx instance
    Vertx vertx = Vertx.vertx();

    //deploy the WebServer verticle
    Future<String> webServerVerticleDeployFuture = vertx.deployVerticle(WebServerVerticle.class.getName());

    // deploy the User data verticle
    Future<String> userDataVerticleDeployFuture = vertx.deployVerticle(UserDataVerticle.class.getName());

    CompositeFuture.all(webServerVerticleDeployFuture, userDataVerticleDeployFuture).onSuccess(event -> {
      System.out.println("List of Futures that were deployed successful are: " + event.list());
    }).onFailure(Throwable::printStackTrace);

    Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
  }
}
