package com.korotin.tasker.validator;

import com.korotin.tasker.domain.DateRange;
import com.korotin.tasker.validator.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {
    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private void setUpErrorMessage(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    @Override
    public boolean isValid(DateRange value, ConstraintValidatorContext context) {
        if (value.getStart() == null) {
            setUpErrorMessage("Start date should not be null", context);
            return false;
        }
        if (value.getEnd() == null) {
            setUpErrorMessage("End date should not be null", context);
            return false;
        }

        if (value.getEnd().isBefore(value.getStart()) || value.getEnd().isEqual(value.getStart())) {
            setUpErrorMessage("Dates should be in chronological order. '%s' should be before '%s'"
                    .formatted(value.getStart(), value.getEnd()), context);
            return false;
        }

        return true;
    }
}
