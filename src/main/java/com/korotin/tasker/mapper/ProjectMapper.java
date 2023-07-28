package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputProjectDTO;
import com.korotin.tasker.domain.dto.ProjectDTO;
import com.korotin.tasker.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = UserMapper.class, componentModel = "spring")
public abstract class ProjectMapper {

    @Autowired
    private UserService userService;

    public abstract OutputProjectDTO projectToOutputDTO(Project project);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "idToUser")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "events", ignore = true)
    public abstract Project DTOToProject(ProjectDTO dto);

    @Named("idToUser")
    public User mapDTOToProject(UUID uuid) {
        return userService.findById(uuid).orElseThrow();
    }

}
