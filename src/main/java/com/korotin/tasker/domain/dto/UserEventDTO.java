package com.korotin.tasker.domain.dto;

import com.korotin.tasker.domain.DateRange;
import com.korotin.tasker.validator.annotation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserEventDTO {

    @NotNull
    @NotBlank
    public String name;

    @NotNull
    public String description;

    @NotNull
    @ValidDateRange
    public DateRange dateRange;
}
