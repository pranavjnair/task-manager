package com.taskmanager.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.taskmanager.exception.AppException;
import com.taskmanager.model.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.mongodb.client.model.Filters.*;

import java.util.Arrays;
import java.util.Optional;

/**
 * user repository that handles the creating and reading of a user. Also contains a singleton implementation.
 */
public class UserRepositoryMongoImpl implements UserRepository {
    private static Logger log = LoggerFactory.getLogger(UserRepositoryMongoImpl.class);

    private static UserRepositoryMongoImpl instance; //reference to itself

    //variables
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> userCollection;
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * part of the singleton implementation - prevent other classes from instantiating this object
     */
    private UserRepositoryMongoImpl() {
    }

    /**
     * part of the singleton implementation - synchronized is added for thread safety
     * @return returns one instance of UserRepositoryMongoImpl so the class is only initialized once
     */
    synchronized public static UserRepositoryMongoImpl getInstance() {
        if (instance == null) {
            try {
                // getting server config from environment variables
                String hostName = System.getenv("MONGO_HOST_NAME");
                Integer portNumber = Integer.parseInt(System.getenv("MONGO_PORT_NUMBER"));
                String databaseName = System.getenv("MONGO_DBNAME");

                instance = new UserRepositoryMongoImpl();
                instance.mongoClient = MongoClients.create(
                        MongoClientSettings.builder()
                                .applyToClusterSettings(builder ->
                                        builder.hosts(Arrays.asList(new ServerAddress(hostName, portNumber)))) // 27017 is the default port
                                .build());
                instance.mongoDatabase = instance.mongoClient.getDatabase(databaseName);
                instance.userCollection = instance.mongoDatabase.getCollection("users");
            }catch (Exception e) {
                throw new AppException("Unable to connect to mongodb.  Possible missing environment variables MONGO_HOST_NAME, MONGO_PORT_NUMBER, MONGO_DBNAME", e);
            }
        }
        return instance;
    }

    /**
     * Inserts a user into the collection
     * @param user - user with parameters
     */
    @Override
    public void create(User user) {
        Document document = new Document("_id", user.getId())
                .append("id", user.getId())
                .append("password", user.getPassword())
                .append("email", user.getEmail())
                .append("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("createTimestamp", user.getCreateTimestamp())
                .append("updateTimestamp", user.getUpdateTimestamp());

        userCollection.insertOne(document);
    }

    /**
     * Reads a user from the database and returns an optional user
     * @param id - id_ stored in the collection of the user
     * @return - Optional<user> that contains the user
     */
    @Override
    public Optional<User> read(String id) {
        try {
            Document document = userCollection.find(eq("_id", id)).first(); // finds user information with id
            if (document == null) {
                return Optional.empty(); // returns optional empty instead of returning null
            }
            String documentJson = document.toJson();
            User user = objectMapper.readValue(documentJson, User.class); // reads value using Jackson
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Cannot find user. User:{}. Error:{}", id, e);
            e.printStackTrace();
            throw new AppException("Cannot find user", e);
        }
    }
}
