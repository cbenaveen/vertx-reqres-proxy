package com.vertx.reqres_proxy.handlers.events;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserDetailsByIdEventHandler implements Handler<Message<JsonObject>> {
    private static final Logger log = LoggerFactory.getLogger(GetUserDetailsByIdEventHandler.class);

    private final WebClient webClient;

    public GetUserDetailsByIdEventHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void handle(Message<JsonObject> event) {
        String uid = event.body().getString("uid");
        log.info("GetUserDetailsByIdEventHandler: Request received to fetch details of user " + uid);
        webClient
                .getAbs("https://reqres.in/api/users/" + uid)
                .send()
                .onSuccess(event1 -> {
                    String body = event1.bodyAsString();
                    System.out.println("GetUserDetailsByIdEventHandler: WebClient response: " + body);

                    JsonObject responseJson = new JsonObject().put("result", body);
                    event.reply(responseJson);
                })
                .onFailure(throwable -> {
                    System.err.println("Exception occurred:" + throwable.getMessage());
                    event.fail(1, throwable.getMessage());
                })
        ;
    }
}
