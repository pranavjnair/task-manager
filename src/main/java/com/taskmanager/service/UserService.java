package com.taskmanager.service;

import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.utils.SHAEncoder;

public class UserService {

    private UserRepository userRepository = new UserRepository();

    public void create(User user)  {

        user.setPassword(SHAEncoder.encodeString(user.getPassword()));

        userRepository.create(user);

    }

    public User get(String userId){

        User user = userRepository.getUser(userId);
        return user;
    }

}
