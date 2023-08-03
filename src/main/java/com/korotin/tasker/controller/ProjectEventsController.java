package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.dto.OutputEventDTO;
import com.korotin.tasker.domain.dto.UserEventDTO;
import com.korotin.tasker.mapper.EventMapper;
import com.korotin.tasker.service.EventService;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ExistingId;
import com.korotin.tasker.validator.annotation.ValidRecord;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/events")
@Validated
public class ProjectEventsController {
    private final EventService eventService;
    private final ProjectService projectService;
    private final EventMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    public List<OutputEventDTO> getAllEvents(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId) {
        return StreamSupport.stream(eventService.findAllByProjectId(projectId).spliterator(), false)
                .map(mapper::eventToDTO)
                .toList();
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    @ValidRecord(recordProvider = EventService.class)
    public OutputEventDTO getEventById(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                       @PathVariable @ExistingId(responsible = EventService.class) UUID eventId) {
        Event event = eventService.findById(eventId).orElseThrow();
        return mapper.eventToDTO(event);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    @Parameter(name = "response", hidden = true)
    public OutputEventDTO createEvent(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                      @Valid @RequestBody UserEventDTO dto,
                                      HttpServletResponse response) {
        Event event = mapper.fromDTO(dto);
        Project project = projectService.findById(projectId).orElseThrow();
        event.setProject(project);

        response.setStatus(HttpServletResponse.SC_CREATED);

        return mapper.eventToDTO(eventService.save(event));
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    @ValidRecord(recordProvider = EventService.class)
    public OutputEventDTO editEvent(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                                    @PathVariable @ExistingId(responsible = EventService.class) UUID eventId,
                                    @Valid @RequestBody UserEventDTO dto) {
        Event event = mapper.fromDTO(dto);
        Project project = projectService.findById(projectId).orElseThrow();
        event.setProject(project);

        return mapper.eventToDTO(eventService.update(eventId, event));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.hasAccess(principal, #projectId)")
    @ValidRecord(recordProvider = EventService.class)
    public void deleteEvent(@PathVariable @ExistingId(responsible = ProjectService.class) UUID projectId,
                            @PathVariable @ExistingId(responsible = EventService.class) UUID eventId) {
        eventService.delete(eventId);
    }



}
