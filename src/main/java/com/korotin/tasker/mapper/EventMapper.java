package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.dto.EventDTO;
import com.korotin.tasker.domain.dto.OutputEventDTO;
import com.korotin.tasker.service.ProjectService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = UserMapper.class, componentModel = "spring")
public abstract class EventMapper {

    @Autowired
    private ProjectService projectService;

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "dateRange.start", source = "startDate")
    @Mapping(target = "dateRange.end", source = "endDate")
    public abstract OutputEventDTO eventToDTO(Event event);

    @Mapping(target = "project", source = "projectId", qualifiedByName = "idToProject")
    @Mapping(target = "startDate", source = "dateRange.start")
    @Mapping(target = "endDate", source = "dateRange.end")
    public abstract Event fromDTO(EventDTO dto);

    @Named("idToProject")
    public Project getProject(UUID uuid) {
        return projectService.findById(uuid).orElseThrow();
    }
}
