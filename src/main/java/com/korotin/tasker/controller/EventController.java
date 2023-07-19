package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.dto.OutputEventDTO;
import com.korotin.tasker.mapper.EventMapper;
import com.korotin.tasker.service.EventService;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ExistingId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final ProjectService projectService;
    private final EventMapper mapper;

    @GetMapping("/{eventId}")
    public OutputEventDTO getEventById(@PathVariable @ExistingId(responsible = EventService.class)UUID eventId) {
        Event event = eventService.findById(eventId).orElseThrow();
        return mapper.eventToDTO(event);
    }
}
