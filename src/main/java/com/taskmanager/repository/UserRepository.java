package com.taskmanager.repository;

import com.taskmanager.model.User;

import java.util.Optional;

/**
 * interface for user repository
 */
public interface UserRepository {
    void create(User user);
    Optional<User> read(String id);

}
