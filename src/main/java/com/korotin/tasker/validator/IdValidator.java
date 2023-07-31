package com.korotin.tasker.validator;

import com.korotin.tasker.exception.NotFoundException;
import com.korotin.tasker.service.CrudService;
import com.korotin.tasker.validator.annotation.ExistingId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdValidator implements ConstraintValidator<ExistingId, UUID> {
    private final ApplicationContext context;
    private CrudService<?, UUID> service;

    @Override
    public void initialize(ExistingId constraintAnnotation) {
        var validator = constraintAnnotation.responsible();
        this.service = context.getBean(validator);

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        // todo optimization: existsById
        if (service.findById(value).isPresent()) {
            return true;
        }

        throw new NotFoundException("Entity with id '%s' could not be found".formatted(value));
    }
}
