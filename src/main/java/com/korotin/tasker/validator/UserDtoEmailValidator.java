package com.korotin.tasker.validator;

import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.exception.ConflictException;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.UniqueUserEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoEmailValidator implements ConstraintValidator<UniqueUserEmail, UserDTO> {

    private final UserService userService;

    @Override
    public void initialize(UniqueUserEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(UserDTO value, ConstraintValidatorContext context) {
        if (userService.findByEmail(value.getEmail()).isEmpty()) {
            return true;
        }

        throw new ConflictException("User with email '%s' already exists".formatted(value.getEmail()));
    }
}
