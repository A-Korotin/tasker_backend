package com.korotin.tasker.service;


import com.korotin.tasker.domain.User;

public interface UserService {
    User register(User user);
    User save(User user);
}
