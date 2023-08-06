package com.korotin.tasker.repository;

import com.korotin.tasker.domain.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends CrudRepository<Task, UUID> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM task t WHERE t.project_id = :project_id")
    Iterable<Task> findAllByProject(@Param("project_id") UUID projectId);
}
