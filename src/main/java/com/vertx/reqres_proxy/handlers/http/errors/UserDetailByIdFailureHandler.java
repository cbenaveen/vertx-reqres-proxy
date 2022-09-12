package com.vertx.reqres_proxy.handlers.http.errors;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDetailByIdFailureHandler implements Handler<RoutingContext> {
    private static final Logger log = LoggerFactory.getLogger(UserDetailByIdFailureHandler.class);

    @Override
    public void handle(RoutingContext event) {
        Throwable t = event.failure();
        log.error("Unexpected error/exception happened in handling the user detail fetch request", t);

        if (t instanceof NumberFormatException) {
            event.response().setStatusCode(500).end("Its not an number");
        } else {
            event.response().setStatusCode(500).end("Invalid input or something else happened");
        }
    }
}
