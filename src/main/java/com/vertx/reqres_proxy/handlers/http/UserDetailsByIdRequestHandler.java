package com.vertx.reqres_proxy.handlers.http;

import com.vertx.reqres_proxy.verticles.UserDataVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserDetailsByIdRequestHandler implements Handler<RoutingContext> {
  private final EventBus eventBus;

  public UserDetailsByIdRequestHandler(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    System.out.println("Routing Content hc: " + routingContext.hashCode());

    String uid = routingContext.pathParam("uid");

    int anInt = Integer.parseInt(uid);
    if (anInt < 1) {
      routingContext.response()
        .setStatusCode(500)
        .end("Invalid User ID.");
    } else {

    eventBus.request(UserDataVerticle.GET_USER_DETAILS_ADDRESS, new JsonObject().put("uid", uid))
      .onSuccess(event -> {
        routingContext.response()
          .end(event.body().toString());
      }).onFailure(throwable -> routingContext.response().setStatusCode(500).end(throwable.toString()));
  }}
}
