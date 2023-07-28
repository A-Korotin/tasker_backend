package com.korotin.tasker.domain.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ExistingId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    @ExistingId(responsible = ProjectService.class)
    public UUID projectId;

    @NotNull
    @NotBlank
    public String name;

    @NotNull
    public String description;

    @NotNull
    public ZonedDateTime startDate;

    @NotNull
    public Boolean done;
}
