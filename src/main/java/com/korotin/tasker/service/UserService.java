package com.korotin.tasker.service;


import com.korotin.tasker.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    User update(UUID id, User value);
    void delete(UUID id);
    Iterable<User> findAll();
}
