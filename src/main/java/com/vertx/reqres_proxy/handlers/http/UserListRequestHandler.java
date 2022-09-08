package com.vertx.reqres_proxy.handlers.http;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class UserListRequestHandler implements Handler<RoutingContext> {
  private final EventBus eventBus;

  public UserListRequestHandler(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    routingContext.response().end("I am UserListRequestHandler");
  }
}
