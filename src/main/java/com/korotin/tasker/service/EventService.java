package com.korotin.tasker.service;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.Task;

import java.util.UUID;

public interface EventService extends CrudService<Event, UUID> {
    Iterable<Event> findAllByProjectId(UUID projectId);

}
