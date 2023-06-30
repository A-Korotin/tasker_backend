package com.korotin.tasker.repository;

import com.korotin.tasker.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Crud Repository for {@link User} domain entity
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
}
