package com.korotin.tasker.domain.dto;

import com.korotin.tasker.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Default user entity output DTO (password is hidden)
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    public UUID id;
    public String name;
    public String email;
    public UserRole role;
}
