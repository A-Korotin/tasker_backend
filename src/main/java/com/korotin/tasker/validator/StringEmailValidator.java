package com.korotin.tasker.validator;

import com.korotin.tasker.exception.ConflictException;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.UniqueUserEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private final UserService userService;

    @Override
    public void initialize(UniqueUserEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userService.findByEmail(value).isEmpty()) {
            return true;
        }
        throw new ConflictException("User with email '%s' already exists".formatted(value));
    }
}
