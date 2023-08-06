package com.korotin.tasker.repository;

import com.korotin.tasker.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Crud repository for {@link Event} domain entity
 */
@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM \"event\" t WHERE t.project_id = :project_id")
    Iterable<Event> findAllByProject(@Param("project_id") UUID projectId);
}
