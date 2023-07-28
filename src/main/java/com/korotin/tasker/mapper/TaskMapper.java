package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.Task;
import com.korotin.tasker.domain.dto.OutputTaskDTO;
import com.korotin.tasker.domain.dto.TaskDTO;
import com.korotin.tasker.service.ProjectService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    @Autowired
    private ProjectService projectService;

    @Mapping(target = "projectId", source = "project.id")
    public abstract OutputTaskDTO taskToDTO(Task task);

    @Mapping(target = "project", source = "projectId", qualifiedByName = "idToProject")
    public abstract Task fromDTO(TaskDTO dto);

    @Named("idToProject")
    public Project idToProject(UUID id) {
        return projectService.findById(id).orElseThrow();
    }
}
