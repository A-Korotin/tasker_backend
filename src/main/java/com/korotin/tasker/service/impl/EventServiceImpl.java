package com.korotin.tasker.service.impl;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.repository.EventRepository;
import com.korotin.tasker.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Event save(Event entity) {
        return eventRepository.save(entity);
    }

    @Override
    public Event update(UUID uuid, Event entity) {
        entity.setId(uuid);
        return eventRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        eventRepository.deleteById(uuid);
    }

    @Override
    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(UUID uuid) {
        return eventRepository.findById(uuid);
    }
}
