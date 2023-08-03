package com.korotin.tasker.repository;

import com.korotin.tasker.domain.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Crud repository for {@link Project} domain entity
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, UUID> {
    @Query(value = "SELECT owner_id FROM project p WHERE p.id = :projectId",
    nativeQuery = true)
    UUID findOwnerId(@Param("projectId") UUID projectId);
}
