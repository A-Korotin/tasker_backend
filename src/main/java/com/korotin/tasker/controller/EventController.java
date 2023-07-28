package com.korotin.tasker.controller;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.dto.EventDTO;
import com.korotin.tasker.domain.dto.OutputEventDTO;
import com.korotin.tasker.mapper.EventMapper;
import com.korotin.tasker.service.EventService;
import com.korotin.tasker.validator.annotation.ExistingId;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final EventMapper mapper;

    @GetMapping("/{eventId}")
    public OutputEventDTO getEventById(@PathVariable @ExistingId(responsible = EventService.class)UUID eventId) {
        Event event = eventService.findById(eventId).orElseThrow();
        return mapper.eventToDTO(event);
    }

    @PutMapping("/{eventId}")
    public OutputEventDTO editEvent(@PathVariable @ExistingId(responsible = EventService.class) UUID eventId,
                                    @Valid @RequestBody EventDTO dto) {
        Event event = eventService.update(eventId, mapper.fromDTO(dto));
        return mapper.eventToDTO(event);
    }

    @PostMapping
    @Parameter(name = "response", hidden = true)
    public OutputEventDTO createEvent(@Valid @RequestBody EventDTO dto, HttpServletResponse response) {
        Event event = eventService.save(mapper.fromDTO(dto));
        response.setStatus(HttpServletResponse.SC_CREATED);
        return mapper.eventToDTO(event);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable @ExistingId(responsible = EventService.class) UUID eventId) {
        eventService.delete(eventId);
    }

    @GetMapping
    public List<OutputEventDTO> getAllEvents() {
        return StreamSupport.stream(eventService.findAll().spliterator(), false)
                .map(mapper::eventToDTO)
                .toList();
    }

}
