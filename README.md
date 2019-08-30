# task-manager
This project was developed to help me learn the basics of server side programming and database. The APIs were implemented using Java HttpServer with MongoDB as the database. 

## Technologies
- Java 1.8
- MongoDB
- Firebase 
- Postman 
- Robo3T

### Dev Environment Setup
There are 4 environment variables for this project that need to be set up in order to use
```
SERVER_PORT=???   (defaults to 8080) 
MONGO_HOST_NAME=???
MONGO_PORT_NUMBER=27017
MONGO_DBNAME=taskmanagerdb
```

## API End-points

The api is handled in the UserRequestHandler.
This class handles both GET and POST requests.

When there is an error with the code there is a error response code thrown.
These responses are:
HTTP_OK 200, TTP_NOT_FOUND 404, HTTP_ERROR 500, HTTP_DUPLICATE 409, HTTP_METHOD_NOT_ALLOWED 405;

If there is no error it will process requests properly in the UserService method in the service module

## User Service

The user service method uses the information from the repository in order to create or read User objects

## Utils
This part of the code contains utilities that are used through my project. Right now there is little utilities but can be increased when the project is expanded further.

Right now there is a SHA Encoder class in this utils that encodes a string using the SHA-256 algorithm.
The SHA-256 algorithm returns a byte array array at first containing a lot of different symbols, but is later changed to a string of hexadecimal values as a password in a separate method.



