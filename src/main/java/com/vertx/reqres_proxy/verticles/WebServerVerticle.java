package com.vertx.reqres_proxy.verticles;

import com.vertx.reqres_proxy.handlers.http.UserDetailsByIdRequestHandler;
import com.vertx.reqres_proxy.handlers.http.errors.UserDetailByIdFailureHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(WebServerVerticle.class);
  public static final String USERPROXY_API_V_1_USERS = "/userproxy/api/v1/users";

  public WebServerVerticle() {
    logger.info("Instance of WebServerVerticle created successfully.");
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(getVertx());

//    router.route("/userproxy/api/v1/users").handler(new UserListRequestHandler(getVertx().eventBus()));

    router.route(USERPROXY_API_V_1_USERS + "/:uid")
      .failureHandler(new UserDetailByIdFailureHandler())
      .handler(new UserDetailsByIdRequestHandler(getVertx().eventBus()));

    final int port = config().getInteger("port");
    logger.info("Binding the web server to the port {}", port);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port);

    startPromise.complete();
  }
}
