package com.taskmanager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.taskmanager.exception.AppException;
import com.taskmanager.model.User;
import com.taskmanager.service.UserService;
import com.taskmanager.utils.SHAEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Handles GET and POST using the HttpHandler class
 */
public class UserRequestHandler implements HttpHandler {
    private static Logger log = LoggerFactory.getLogger(UserRequestHandler.class);

    public static int HTTP_OK = 200;
    public static int HTTP_NOT_FOUND = 404;
    public static int HTTP_ERROR = 500;
    public static int HTTP_DUPLICATE = 409;
    public static int HTTP_METHOD_NOT_ALLOWED = 405;

    private UserService userService = new UserService();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles GET and POST using a switch statement
     * @param httpExchange - handler for the HTTP requests
     */
    @Override
    public void handle(HttpExchange httpExchange) {
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

    /**
     * Request that processes method not allowed
     * @param httpExchange - Handler for HTTP requests
     */
    private void processUnknownRequest(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(HTTP_METHOD_NOT_ALLOWED, 0);
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * Request that processes duplicate users
     * @param httpExchange - Handler for HTTP requests
     */
    private void processDuplicate(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(HTTP_DUPLICATE, 0);  //
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * Request that processes errors
     * @param httpExchange - Handler for HTTP requests
     */
    private void processError(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(HTTP_ERROR, 0);
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * Request that processes methods not found
     * @param httpExchange - Handler for HTTP requests
     */
    private void processNotFound(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(HTTP_NOT_FOUND, 0);
        } catch (Exception e) {
            log.error("Exception:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * Gets the user and sends a response in json format
     * @param httpExchange - Handler for HTTP requests
     */
    private void getUser(HttpExchange httpExchange) {
        try {
            log.info("url:{}", httpExchange.getRequestURI().getPath());
            String[] pathTokens = httpExchange.getRequestURI().getPath().split("/");
            String id = pathTokens[pathTokens.length - 1];
            log.info("id:{}", id);

            Optional<User> optionalUser = userService.get(id);
            if (optionalUser.isPresent()) {
                Headers headers = httpExchange.getResponseHeaders();
                headers.set("Content-Type", "application/json; charset=UTF-8");
                User user = optionalUser.get();
                String jsonUser = objectMapper.writeValueAsString(user);
                sendResponse(httpExchange, jsonUser);
            } else {
                processNotFound(httpExchange);
            }
        } catch (Exception e) {
            log.error("Error processing request. Error::", e);
            e.printStackTrace();
            processError(httpExchange);
        }
    }


    /**
     * Posts the user
     * @param httpExchange - Handler for HTTP requests
     */
    private void postUser(HttpExchange httpExchange) {
        log.info("in PostUser:{}", httpExchange.getRequestURI().getPath());
        try {

            String userString = getRequestBodyAsString(httpExchange);

            User user = objectMapper.readValue(userString, User.class);

            userService.create(user);

            Optional<User> userOptional = userService.get(user.getId());

            String jsonUser = objectMapper.writeValueAsString(userOptional.get());
            sendResponse(httpExchange, jsonUser);
        } catch (Exception e) {
            log.error("Error processing request. Error::", e);
            e.printStackTrace();
            processError(httpExchange);
        }
    }

    /**
     * helper method for getting request body
     * @param httpExchange - Handler for HTTP requests
     * @return - returns the String of the byteArrayOutputStream
     */
    private String getRequestBodyAsString(HttpExchange httpExchange) {
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            byteArrayOutputStream.close();

            return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new AppException("Unable to get request body", e);
        }
    }

    /**
     * Request that processes HTTP 200s
     * @param httpExchange - Handler for HTTP requests
     * @param json - string with json formatting
     * @throws IOException - exception that doesn't need to be dealt with
     */
    private void sendResponse(HttpExchange httpExchange, String json) throws IOException {
        httpExchange.sendResponseHeaders(HTTP_OK, json.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(json.getBytes());
        outputStream.flush();
    }

}
