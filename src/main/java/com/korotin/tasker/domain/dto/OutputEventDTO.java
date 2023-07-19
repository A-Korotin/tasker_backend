package com.korotin.tasker.domain.dto;

import com.korotin.tasker.domain.DateRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutputEventDTO {
    public UUID id;
    public UUID projectId;
    public String name;
    public String description;
    public DateRange dateRange;

}
