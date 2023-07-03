package com.korotin.tasker.service;

import com.korotin.tasker.domain.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Abstract service with basic CRUD operations
 * @param <T> Service entity type (e.g. {@link User})
 * @param <ID> Service entity ID type (e.g. {@link Long} or {@link UUID})
 */
public interface CrudService<T, ID> {
    T save(T entity);
    T update(ID id, T entity);
    void delete(ID id);
    Iterable<T> findAll();
    Optional<T> findById(ID id);
}
