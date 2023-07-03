package com.korotin.tasker.service;

import java.util.Optional;

public interface CrudService<T, ID> {
    T save(T entity);
    T update(ID id, T entity);
    void delete(ID id);
    Iterable<T> findAll();
    Optional<T> findById(ID id);
}
