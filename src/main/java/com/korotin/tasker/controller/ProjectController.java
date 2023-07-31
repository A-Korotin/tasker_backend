package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.dto.OutputProjectDTO;
import com.korotin.tasker.domain.dto.ProjectDTO;
import com.korotin.tasker.mapper.ProjectMapper;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ExistingId;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@Validated
@Secured("ROLE_ADMIN")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper mapper;

    @GetMapping("/{projectId}")
    public OutputProjectDTO getProjectById(@PathVariable
                                               @ExistingId(responsible = ProjectService.class) UUID projectId) {
        Project project = projectService.findById(projectId).orElseThrow();

        return mapper.projectToOutputDTO(project);
    }

    @Parameter(name = "response", hidden = true)
    @PostMapping
    public OutputProjectDTO createProject(@Valid @RequestBody ProjectDTO dto, HttpServletResponse response) {
        Project project = projectService.save(mapper.DTOToProject(dto));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return mapper.projectToOutputDTO(project);
    }

    @PutMapping("/{projectId}")
    public OutputProjectDTO editProject(@Valid @RequestBody ProjectDTO dto,
                                        @PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId) {
        Project project = projectService.update(projectId, mapper.DTOToProject(dto));
        return mapper.projectToOutputDTO(project);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId) {
        projectService.delete(projectId);
    }

    @GetMapping
    public List<OutputProjectDTO> getAllProjects() {
        return StreamSupport.stream(projectService.findAll().spliterator(), false)
                .map(mapper::projectToOutputDTO)
                .toList();
    }
}
