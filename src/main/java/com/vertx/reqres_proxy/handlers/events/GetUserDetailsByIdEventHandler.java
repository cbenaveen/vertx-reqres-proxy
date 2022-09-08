package com.vertx.reqres_proxy.handlers.events;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class GetUserDetailsByIdEventHandler implements Handler<Message<JsonObject>> {
  private final WebClient webClient;

  public GetUserDetailsByIdEventHandler(WebClient webClient) {
    this.webClient  = webClient;
  }
@Override
public void handle(Message<JsonObject> event) {
  String uid = event.body().getString("uid");
  System.out.println("GetUserDetailsByIdEventHandler: Request received to fetch details of user " + uid);
  webClient
    .getAbs("https://reqresddd.in/api/users/" + uid)
    .send(new Handler<AsyncResult<HttpResponse<Buffer>>>() {
      @Override
      public void handle(AsyncResult<HttpResponse<Buffer>> asyncResult) {
        if (asyncResult.succeeded()) {
              String body = asyncResult.result().bodyAsString();
              System.out.println("GetUserDetailsByIdEventHandler: WebClient response: " + body);

              JsonObject responseJson = new JsonObject().put("result", body);
              event.reply(responseJson);
        }
        else {
                System.err.println("Exception occurred:" + asyncResult.cause().getMessage());
                event.fail(1, asyncResult.cause().getMessage());
        }
      }
    })
//    .onSuccess(event1 -> {
//    String body = event1.bodyAsString();
//    System.out.println("GetUserDetailsByIdEventHandler: WebClient response: " + body);
//
//    JsonObject responseJson = new JsonObject().put("result", body);
//    event.reply(responseJson);
//  })
//    .onFailure(throwable -> {
//      System.err.println("Exception occurred:" + throwable.getMessage());
//      event.fail(1, throwable.getMessage());
//    } )
  ;
}
}
