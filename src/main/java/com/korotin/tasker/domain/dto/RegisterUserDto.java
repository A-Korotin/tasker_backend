package com.korotin.tasker.domain.dto;

import com.korotin.tasker.validator.annotation.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Registration request body entity
 */
@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {

    @NotNull
    @NotBlank
    public String name;

    @NotNull
    @NotBlank
    @Email
    @UniqueUserEmail
    public String email;

    @NotNull
    @NotBlank
    @Size(min = 5)
    public String password;
}
