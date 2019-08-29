# task-manager
This project was made to help me learn basic server and database management.
Using MongoDB I was able to learn how to use GET and POST from Postman in order to take and push data from a database.
I used an application called Robo 3T in order to see the database.
During this project I also learned about a class type called singleton in which only one instance is created of a given class.
I created this class using a synchronous implementation.
I came to understand how important using a singleton class is because when using a database there isn't any need for initializing it more than once with a reference.

On a side note, I tried to use a Firebase database implementation originally, but after learning more about these databases,
I came to the conclusion that MongoDB was the more efficient and less time consuming way of implementing my User Repository


## API Overview

### Setting up dev environment
Setup these environment variables before starting the app.




```
SERVER_PORT=???   (defaults to 8080)
MONGO_HOST_NAME=???
MONGO_PORT_NUMBER=27017
MONGO_DBNAME=taskmanagerdb
```