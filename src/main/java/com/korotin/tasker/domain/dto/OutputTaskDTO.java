package com.korotin.tasker.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OutputTaskDTO {
    public UUID id;
    public UUID projectId;
    public String name;
    public String description;
    public ZonedDateTime startDate;
    public Boolean done;
}
