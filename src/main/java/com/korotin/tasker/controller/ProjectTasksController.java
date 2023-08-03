package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.Task;
import com.korotin.tasker.domain.dto.OutputTaskDTO;
import com.korotin.tasker.domain.dto.UserTaskDTO;
import com.korotin.tasker.mapper.TaskMapper;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.service.TaskService;
import com.korotin.tasker.validator.annotation.ExistingId;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

// todo 03.08.2023: check if task belongs to the specified project
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/tasks")
@Validated
public class ProjectTasksController {
    private final TaskService taskService;
    private final TaskMapper mapper;
    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    public List<OutputTaskDTO> getAllTasks(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId) {
        return StreamSupport.stream(taskService.findAllByProjectId(projectId).spliterator(), false)
                .map(mapper::taskToDTO)
                .toList();
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    public OutputTaskDTO getTaskById(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                     @PathVariable @ExistingId(responsible = TaskService.class) UUID taskId) {
        Task task = taskService.findById(taskId).orElseThrow();
        return mapper.taskToDTO(task);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    @Parameter(name = "response", hidden = true)
    public OutputTaskDTO createTask(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                    @Valid @RequestBody UserTaskDTO dto,
                                    HttpServletResponse response) {
        Task task = mapper.fromDTO(dto);
        Project project = projectService.findById(projectId).orElseThrow();
        task.setProject(project);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return mapper.taskToDTO(taskService.save(task));

    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    public OutputTaskDTO editTask(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                  @PathVariable @ExistingId(responsible = TaskService.class) UUID taskId,
                                  @Valid @RequestBody UserTaskDTO dto) {
        Task task = mapper.fromDTO(dto);
        Project project = projectService.findById(projectId).orElseThrow();
        task.setProject(project);
        return mapper.taskToDTO(taskService.update(taskId, task));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    public void deleteTask(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                           @PathVariable @ExistingId(responsible = TaskService.class) UUID taskId) {
        taskService.delete(taskId);
    }
}
