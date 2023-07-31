package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Task;
import com.korotin.tasker.domain.dto.OutputTaskDTO;
import com.korotin.tasker.domain.dto.TaskDTO;
import com.korotin.tasker.mapper.TaskMapper;
import com.korotin.tasker.service.TaskService;
import com.korotin.tasker.validator.annotation.ExistingId;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@Validated
@Secured("ROLE_ADMIN")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper mapper;

    @GetMapping("/{taskId}")
    public OutputTaskDTO getTaskById(@PathVariable @ExistingId(responsible = TaskService.class) UUID taskId) {
        Task task = taskService.findById(taskId).orElseThrow();
        return mapper.taskToDTO(task);
    }

    @PutMapping("/{taskId}")
    public OutputTaskDTO editTask(@PathVariable @ExistingId(responsible = TaskService.class) UUID taskId,
                                  @Valid @RequestBody TaskDTO dto) {
        Task task = taskService.update(taskId, mapper.fromDTO(dto));
        return mapper.taskToDTO(task);
    }

    @PostMapping
    @Parameter(name = "response", hidden = true)
    public OutputTaskDTO createTask(@Valid @RequestBody TaskDTO dto, HttpServletResponse response) {
        Task task = taskService.save(mapper.fromDTO(dto));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return mapper.taskToDTO(task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable @ExistingId(responsible = TaskService.class) UUID taskId) {
        taskService.delete(taskId);
    }

    @GetMapping
    public List<OutputTaskDTO> getAllTasks() {
        return StreamSupport.stream(taskService.findAll().spliterator(), false)
                .map(mapper::taskToDTO)
                .toList();
    }
}
