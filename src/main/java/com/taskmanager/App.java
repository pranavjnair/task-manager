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
        log.info("starting server");

        try {
            int serverPort = 8080;
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            server.createContext("/users", new UserRequestHandler());
            server.start();
        } catch (Exception e) {
            log.error("Unable to start server. Error: {}", e);
        }

        log.info("started server");
    }
}
