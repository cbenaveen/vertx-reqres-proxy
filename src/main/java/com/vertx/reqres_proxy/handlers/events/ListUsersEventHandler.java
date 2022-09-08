package com.vertx.reqres_proxy.handlers.events;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class ListUsersEventHandler  implements Handler<Message<JsonObject>> {
  private final WebClient webClient;

  public ListUsersEventHandler(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
public void handle(Message<JsonObject> event) {
  }
}
