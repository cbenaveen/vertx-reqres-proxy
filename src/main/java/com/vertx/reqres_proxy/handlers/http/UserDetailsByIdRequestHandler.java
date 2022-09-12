package com.vertx.reqres_proxy.handlers.http;

import com.vertx.reqres_proxy.verticles.UserDataVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDetailsByIdRequestHandler implements Handler<RoutingContext> {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsByIdRequestHandler.class);
    private final EventBus eventBus;

    public UserDetailsByIdRequestHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String uid = routingContext.pathParam("uid");
        logger.info("Request received to fetch the user details for the user with id {}", uid);

        int anInt = Integer.parseInt(uid);
        if (anInt < 1) {
            logger.warn("The user id {} is invalid. Responding the request with status code 500", uid);
            routingContext.response().setStatusCode(500)
                    .end("Invalid User ID.");
        } else {
            eventBus.request(UserDataVerticle.GET_USER_DETAILS_ADDRESS, new JsonObject().put("uid", uid))
                    .onSuccess(event -> {
                        logger.info("Received a use details response in the event bus for the user id {}, " +
                                "the response is {}", uid, ((JsonObject) event.body()).encodePrettily());
                        routingContext.response().end(event.body().toString());
                    }).onFailure(throwable -> {
                        logger.warn("Received a failure response in event bus for the user detail fetch with id {}", uid,
                                throwable);
                        routingContext.response().setStatusCode(500).end(throwable.toString());
                    });
        }
    }
}
