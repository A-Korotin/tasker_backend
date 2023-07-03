package com.korotin.tasker.domain.dto;

import com.korotin.tasker.service.UserService;
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
public class ProjectDTO {
    @NotNull
    @NotBlank
    public String name;

    @NotNull
    @ExistingId(responsible = UserService.class)
    public UUID ownerId;
}
