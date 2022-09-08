package com.vertx.reqres_proxy.verticles;

import com.vertx.reqres_proxy.handlers.events.GetUserDetailsByIdEventHandler;
import com.vertx.reqres_proxy.handlers.events.ListUsersEventHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class UserDataVerticle extends AbstractVerticle {
  public static final String LIST_USER_ADDRESS = "proxy.users.list";
  public static final String GET_USER_DETAILS_ADDRESS = "proxy.users.get.details";

  public void start(Promise<Void> startPromise) throws Exception {
    WebClientOptions webClientOptions = new WebClientOptions();
    WebClient webClient = WebClient.create(vertx, webClientOptions);

    vertx.eventBus().consumer(LIST_USER_ADDRESS, new ListUsersEventHandler(webClient));
    vertx.eventBus().consumer(GET_USER_DETAILS_ADDRESS, new GetUserDetailsByIdEventHandler(webClient));
  }


}
