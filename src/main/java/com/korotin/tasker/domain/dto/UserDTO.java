package com.korotin.tasker.domain.dto;

import com.korotin.tasker.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * General input User DTO
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDTO {

    @NotNull
    @NotBlank
    public String name;

    @NotNull
    @NotBlank
    @Email
    public String email;

    @NotNull
    @NotBlank
    @Size(min = 5)
    public String password;

    public UserRole role;
}
