package com.taskmanager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;


public class UserRequestHandler implements HttpHandler {

    static Logger log = LoggerFactory.getLogger(UserRequestHandler.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                getUser(httpExchange);
                break;
            case "POST":
                postUser(httpExchange);
            default:
                processUnknownRequest(httpExchange);
        }
    }

    private void processUnknownRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(405, 0);
    }

    private void getUser(HttpExchange httpExchange) throws IOException {

        log.info("url:{}", httpExchange.getRequestURI().getPath());
        String[] pathTokens = httpExchange.getRequestURI().getPath().split("/");
        String id = pathTokens[pathTokens.length - 1];
        log.info("id:{}", id);

        User user = new User("pranavnair", "abc123", "pjn@gmail.com", "pranav", "nair");
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();

    }

    private void postUser(HttpExchange httpExchange) {
//        httpExchange.sendResponseHeaders(200, response.getBytes().length);
//        Headers headers = httpExchange.getResponseHeaders();
//        headers.set("Content-Type", "application/json; charset=UTF-8");
//        OutputStream outputStream = httpExchange.getResponseBody();
//        outputStream.write(response.getBytes());
//        outputStream.flush();

    }

}
