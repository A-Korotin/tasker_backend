package com.korotin.tasker.validator;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.domain.dto.UserDTO;
import com.korotin.tasker.exception.ConflictException;
import com.korotin.tasker.service.UserService;
import com.korotin.tasker.validator.annotation.ValidUserEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class EditUserEmailValidator implements ConstraintValidator<ValidUserEmail, Object[]> {

    private final UserService userService;

    private int idIndex;
    private int dtoIndex;

    @Override
    public void initialize(ValidUserEmail constraintAnnotation) {
        this.idIndex = constraintAnnotation.idIndex();
        this.dtoIndex = constraintAnnotation.dtoIndex();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value.length <= Math.max(idIndex, dtoIndex))
            throw new IllegalArgumentException("Parameter size does not fit desired index (%d)"
                    .formatted(Math.max(idIndex, dtoIndex)));

        if (!(value[idIndex] instanceof UUID id) || !(value[dtoIndex] instanceof UserDTO userDTO)) {
            throw new IllegalArgumentException("Method signature does not match expected format");
        }

        Optional<User> existingUser = userService.findByEmail(userDTO.getEmail());

        if (existingUser.isEmpty()) {
            return true;
        }

        if (existingUser.get().getId().equals(id)) {
            return true;
        }

        throw new ConflictException("User with email '%s' already exists".formatted(userDTO.getEmail()));
    }
}
