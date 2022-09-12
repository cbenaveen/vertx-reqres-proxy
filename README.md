# vertx-reqres-proxy
A Proxy style application written in Vertx to consume the User related data from reqres.in

# APIs
## Get User details by ID:
```
curl --location \
     --request GET 
     'http://localhost:8080/userproxy/api/v1/users/1'
     
Note: Replace the 1 with a int > 0
```

# Running option
## Standalone Mode:
Run the application in standalone mode using `UserDetailsProxyApp`

## Cluster Mode
In cluster mode, you can run any instance of this application using `ClusteredUserDetailsProxyApp`.
### Run application with Web Server:
```shell
# Deploy vertx application with Web verticle

Open terminal/cmd
VERTX_CONFIG_PATH=src/main/resources/conf/cluster-config-with-webserver.json \
  mvn compile exec:java \
  -Dexec.mainClass="com.vertx.reqres_proxy.ClusteredUserDetailsProxyApp"
  
# Upon successful running of the app, you can observe the below log line
2022-09-12 17:07:37 INFO  WebServerVerticle - Instance of WebServerVerticle created successfully.
2022-09-12 17:07:37 INFO  WebServerVerticle - Binding the web server to the port 8080
```

### Run one or N number of instances without Web Server
```shell
# Deploy vertx application with Web verticle

Open terminal/cmd
VERTX_CONFIG_PATH=src/main/resources/conf/cluster-config-without-webserver.json \
  mvn compile exec:java \
  -Dexec.mainClass="com.vertx.reqres_proxy.ClusteredUserDetailsProxyApp"
  
# Upon successful running of the app, you can observe the below log line
2022-09-12 17:10:07 INFO  ClusteredUserDetailsProxyApp - Skipping the web server verticle deployment as the configuration deployWebServer set to false
```

###  Testing
When the application running in Cluster mode, make the API request and see that the event handler running in different instance is consuming the event and processing.
The request are sent to handlers in round-robin. 