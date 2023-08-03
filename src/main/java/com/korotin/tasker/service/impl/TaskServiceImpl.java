package com.korotin.tasker.service.impl;

import com.korotin.tasker.domain.Task;
import com.korotin.tasker.repository.TaskRepository;
import com.korotin.tasker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public Task update(UUID uuid, Task entity) {
        entity.setId(uuid);
        return taskRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        taskRepository.deleteById(uuid);
    }

    @Override
    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(UUID uuid) {
        return taskRepository.findById(uuid);
    }

    @Override
    public Iterable<Task> findAllByProjectId(UUID projectId) {
        return taskRepository.findAllByProject(projectId);
    }
}
