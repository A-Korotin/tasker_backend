package com.korotin.tasker.repository;

import com.korotin.tasker.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Crud repository for {@link Event} domain entity
 */
@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {
}
