package com.korotin.tasker.repository;

import com.korotin.tasker.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Crud repository for {@link Project} domain entity
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, UUID> {
}
