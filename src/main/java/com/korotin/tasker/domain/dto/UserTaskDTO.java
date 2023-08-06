package com.korotin.tasker.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserTaskDTO {
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
