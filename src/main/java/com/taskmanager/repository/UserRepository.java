package com.taskmanager.repository;

import com.taskmanager.model.User;

import java.util.Optional;

public interface UserRepository {
    void create(User user);
    Optional<User> read(String id);

}
