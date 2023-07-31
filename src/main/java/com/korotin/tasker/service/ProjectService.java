package com.korotin.tasker.service;

import com.korotin.tasker.domain.Project;

import java.util.UUID;

/**
 * Service for {@link Project} domain entity. Contains basic CRUD functionality.
 * @see CrudService
 */
public interface ProjectService extends CrudService<Project, UUID> {

}
