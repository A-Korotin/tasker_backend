package com.korotin.tasker.service;

import com.korotin.tasker.domain.Task;

import java.util.UUID;

public interface TaskService extends CrudService<Task, UUID> {
    Iterable<Task> findAllByProjectId(UUID projectId);
}
