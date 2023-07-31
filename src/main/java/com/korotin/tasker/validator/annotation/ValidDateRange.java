package com.korotin.tasker.validator.annotation;

import com.korotin.tasker.domain.DateRange;
import com.korotin.tasker.validator.DateRangeValidator;
import com.korotin.tasker.validator.StringEmailValidator;
import com.korotin.tasker.validator.UserDtoEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Constraint annotation for {@link DateRange} entity.
 * Ensuring chronological dateTime order
 * {@link DateRange#getStart()} should be earlier than {@link DateRange#getEnd()}
 * Both start and end should not be null
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Date range is valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
