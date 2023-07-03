package com.korotin.tasker.service;


import com.korotin.tasker.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for {@link User} entity.
 * Contains basic CRUD functionality extended with business-specific methods.
 * @see CrudService
 * @see UserDetailsService
 */
public interface UserService extends CrudService<User, UUID>, UserDetailsService {
    Optional<User> findByEmail(String email);
}
