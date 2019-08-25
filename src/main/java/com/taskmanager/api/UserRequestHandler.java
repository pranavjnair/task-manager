package com.taskmanager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.taskmanager.model.User;
import com.taskmanager.service.UserService;
import com.taskmanager.utils.SHAEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class UserRequestHandler implements HttpHandler {

    private static Logger log = LoggerFactory.getLogger(UserRequestHandler.class);
    private UserService userService = new UserService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                getUser(httpExchange);
                break;
            case "POST":
                postUser(httpExchange);
                break;
            default:
                processUnknownRequest(httpExchange);
        }
        httpExchange.close();
    }

    private void processUnknownRequest(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(405, 0);
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }

    private void processError(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(500, 0);
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }


    private void getUser(HttpExchange httpExchange) {

        try {
            log.info("url:{}", httpExchange.getRequestURI().getPath());
            String[] pathTokens = httpExchange.getRequestURI().getPath().split("/");
            String id = pathTokens[pathTokens.length - 1];
            log.info("id:{}", id);

//        User user = new User("pranavnair", "abc123", "pjn@gmail.com", "pranav", "nair");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String response = objectMapper.writeValueAsString(user);
//        httpExchange.sendResponseHeaders(200, response.getBytes().length);
//        Headers headers = httpExchange.getResponseHeaders();
//        headers.set("Content-Type", "application/json; charset=UTF-8");
//        OutputStream outputStream = httpExchange.getResponseBody();
//        outputStream.write(response.getBytes());
//        outputStream.flush();

            User user = userService.get(id);

        } catch (Exception e) {
            log.error("Error processing request. Error::", e);
            e.printStackTrace();
            processError(httpExchange);
        }
    }

    private void postUser(HttpExchange httpExchange) {
        log.info("in PostUser:{}", httpExchange.getRequestURI().getPath());
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream inputStream = httpExchange.getRequestBody();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            byteArrayOutputStream.close();

            String stringData = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
            log.info("JSON-input:{}", stringData);

            User user = objectMapper.readValue(stringData, User.class);
            log.info("user:{}", user);

            userService.create(user);

            httpExchange.sendResponseHeaders(200, user.toString().length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(user.toString().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            log.error("Error processing request. Error::", e);
            e.printStackTrace();
            processError(httpExchange);
        }
    }

}
