package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.OutputProjectDTO;
import com.korotin.tasker.domain.dto.ProjectDTO;
import com.korotin.tasker.mapper.ProjectMapper;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.ExistingId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@Validated
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMapper mapper;

    @GetMapping("/{projectId}")
    public OutputProjectDTO getProjectById(@PathVariable
                                               @ExistingId(responsible = ProjectService.class) UUID projectId) {
        Project project = projectService.findById(projectId).orElseThrow();

        return mapper.projectToOutputDTO(project);
    }

    @PostMapping
    public OutputProjectDTO createProject(@Valid @RequestBody ProjectDTO dto) {
        Project project = mapper.DTOToProject(dto);
        User owner = userService.findById(dto.getOwnerId()).orElseThrow();
        project.setOwner(owner);

        project = projectService.save(mapper.DTOToProject(dto));
        return mapper.projectToOutputDTO(project);
    }
}
