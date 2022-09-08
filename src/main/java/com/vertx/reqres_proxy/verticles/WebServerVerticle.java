package com.vertx.reqres_proxy.verticles;

import com.vertx.reqres_proxy.handlers.http.UserDetailsByIdRequestHandler;
import com.vertx.reqres_proxy.handlers.http.UserListRequestHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WebServerVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(getVertx());

//    router.route("/userproxy/api/v1/users").handler(new UserListRequestHandler(getVertx().eventBus()));
    router.route("/userproxy/api/v1/users/:uid")
      .failureHandler(new Handler<RoutingContext>() {
        @Override
        public void handle(RoutingContext event) {
          System.out.println("Routing Content hc: " + event.hashCode());
          Throwable t = event.failure();

          if (t instanceof NumberFormatException) {
            event.response().setStatusCode(500).end("Its not an number");
          } else {
            event.response().setStatusCode(500).end("Invalid input or something else happened");
          }
        }
      })
      .handler(new UserDetailsByIdRequestHandler(getVertx().eventBus()));

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080);
  }
}
