package com.taskmanager.service;

import com.taskmanager.exception.DuplicateException;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.repository.UserRepositoryFirebaseImpl;
import com.taskmanager.repository.UserRepositoryMongoImpl;
import com.taskmanager.utils.SHAEncoder;

import java.time.Instant;
import java.util.Optional;

/**
 * Service class that calls methods in the repository and handles them
 */
public class UserService {

//    private UserRepositoryFirebaseImpl userRepositoryFirebaseImpl = new UserRepositoryFirebaseImpl();

    /**
     * gets an instance of the user repository implementation
     */
    private UserRepository userRepository = UserRepositoryMongoImpl.getInstance();

    /**
     * Creates a user in the repository
     * @param user - user that needs to be added to repository
     */
    public void create(User user) {
        Optional<User> optionalUser = userRepository.read(user.getId()); // uses optional user to read the repostory
        if (optionalUser.isPresent()) throw new DuplicateException("User already exists");

        user.setPassword(SHAEncoder.encodeString(user.getPassword()));
        Instant now = Instant.now();
        user.setCreateTimestamp(now.toString());
        user.setUpdateTimestamp(now.toString());
        userRepository.create(user);
    }

    /**
     * Gets the user using a string of the user id
     * @param userId - string of the user id. Stored as id_
     * @return - returns an optional of the user
     */
    public Optional<User> get(String userId) {
        Optional<User> optionalUser = userRepository.read(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(maskUserPassword(user.getPassword()));
            optionalUser = Optional.of(user);
        }
        return optionalUser;
    }

    /**
     * Masks the user password
     * @param passwordToMask - password that needs to be masked
     * @return - returns a masked password so password is not displayed to user when using GET
     */
    private String maskUserPassword(String passwordToMask) {
        return "XXXXXXX";
    }

}
