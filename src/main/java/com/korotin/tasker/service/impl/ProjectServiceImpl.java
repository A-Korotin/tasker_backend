package com.korotin.tasker.service.impl;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.repository.ProjectRepository;
import com.korotin.tasker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project save(Project entity) {
        return projectRepository.save(entity);
    }

    @Override
    public Project update(UUID uuid, Project entity) {
        entity.setId(uuid);
        return projectRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        projectRepository.deleteById(uuid);
    }

    @Override
    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> findById(UUID uuid) {
        return projectRepository.findById(uuid);
    }
}
