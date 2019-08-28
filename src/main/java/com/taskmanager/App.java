package com.taskmanager;

import com.sun.net.httpserver.HttpServer;
import com.taskmanager.api.UserRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        try {
            Integer port = getPort();
            log.info("Starting server on port::{}", port);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/users", new UserRequestHandler());  //forward user requests to UserRequestHandler
            server.start();
        } catch (Exception e) {
            log.error("Unable to start server. Error: {}", e);
        }
        log.info("Started server");
    }

    private static Integer getPort() {
        int serverPort = 8080; //Default to this port
        try {
            serverPort = Integer.parseInt(System.getenv("SERVER_PORT")); //get port from env var
        } catch (Exception e) {
            //use default
        }
        return serverPort;
    }
}
