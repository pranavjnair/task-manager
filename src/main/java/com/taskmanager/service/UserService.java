package com.taskmanager.service;

import com.taskmanager.exception.DuplicateException;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.repository.UserRepositoryFirebaseImpl;
import com.taskmanager.repository.UserRepositoryMongoImpl;
import com.taskmanager.utils.SHAEncoder;

import java.time.Instant;
import java.util.Optional;

public class UserService {

//    private UserRepositoryFirebaseImpl userRepositoryFirebaseImpl = new UserRepositoryFirebaseImpl();

    private UserRepository userRepository = UserRepositoryMongoImpl.getInstance();


    public void create(User user) {
        Optional<User> optionalUser = userRepository.read(user.getId());
        if (optionalUser.isPresent()) throw new DuplicateException("User already exists");

        user.setPassword(SHAEncoder.encodeString(user.getPassword()));
        Instant now = Instant.now();
        user.setCreateTimestamp(now.toString());
        user.setUpdateTimestamp(now.toString());
        userRepository.create(user);
    }

    public Optional<User> get(String userId) {
        Optional<User> optionalUser = userRepository.read(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(maskUserPassword(user.getPassword()));
            optionalUser = Optional.of(user);
        }
        return optionalUser;
    }

    private String maskUserPassword(String string) {
        return "XXXXXXX";
    }

}
