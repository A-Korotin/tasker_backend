package com.korotin.tasker.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OutputProjectDTO {
    public UUID id;
    public String name;
    public OutputUserDTO owner;
}
