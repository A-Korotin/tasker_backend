package com.korotin.tasker.domain.dto;

import com.korotin.tasker.domain.DateRange;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ExistingId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class EventDTO {

    @ExistingId(responsible = ProjectService.class)
    public UUID projectId;

    @NotNull
    @NotBlank
    public String name;

    @NotNull
    public String description;

    @NotNull
    public DateRange dateRange;
}
