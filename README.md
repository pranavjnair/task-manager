# task-manager
This project was made to help me learn basic server and database management.
Using MongoDB I was able to learn how to use GET and POST from Postman in order to take and push data from a database.
I used an application called Robo 3T in order to see the database.
During this project I also learned about a class type called singleton in which only one instance is created of a given class.
I created this class using a synchronous implementation.
I came to understand how important using a singleton class is because when using a database there isn't any need for initializing it more than once with a reference.

On a side note, I tried to use a Firebase database implementation originally, but after learning more about these databases,
I came to the conclusion that MongoDB was the more efficient and less time consuming way of implementing my User Repository

### Setting up
There are 4 environment variables for this project that need to be set up in order to use
```
SERVER_PORT=???   (defaults to 8080)
MONGO_HOST_NAME=???
MONGO_PORT_NUMBER=27017
MONGO_DBNAME=taskmanagerdb
```

## API Overview

The api is handled in the UserRequestHandler.
This class handles both GET and POST requests.

When there is an error with the code there is a error response code thrown.
These responses are:
HTTP_OK 200, TTP_NOT_FOUND 404, HTTP_ERROR 500, HTTP_DUPLICATE 409, HTTP_METHOD_NOT_ALLOWED 405;

If there is no error it will process requests properly in the UserService method in the service module

## User Service

The user service method uses the information from the repository in order to create or read User objects

## Repository
This part of the code is the deepest layer of the project.

There is two parts of the code, one part was the original database that I worked with in Firebase and then the second database that I moved to in Mongodb.

I chose Firebase initially because I found that it was from google and thought it would be pretty effective.
I then later swapped over to Mongodb because I realized that a lot of the things I was doing with Firebase was more efficient with Mongodb.
Specifically when I was looking to get data from the database I was surprised how much simpler it was in MongoDB than in Firebase.

## Utils
This part of the code contains utilities that are used through my project. Right now there is little utilities but can be increased when the project is expanded further.

Right now there is a SHA Encoder class in this utils that encodes a string using the SHA-256 algorithm.
The SHA-256 algorithm returns a byte array array at first containing a lot of different symbols, but is later changed to a string of hexadecimal values as a password in a separate method.



