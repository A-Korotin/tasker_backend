package com.korotin.tasker.mapper;

import com.korotin.tasker.domain.Event;
import com.korotin.tasker.domain.dto.OutputEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "dateRange.start", source = "startDate")
    @Mapping(target = "dateRange.end", source = "endDate")
    public abstract OutputEventDTO eventToDTO(Event event);
}
