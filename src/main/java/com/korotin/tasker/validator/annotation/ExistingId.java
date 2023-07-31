package com.korotin.tasker.validator.annotation;

import com.korotin.tasker.service.CrudService;
import com.korotin.tasker.validator.IdValidator;
import com.korotin.tasker.validator.StringEmailValidator;
import com.korotin.tasker.validator.UserDtoEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.UUID;

/**
 * Input value constraint for ID.
 */
@Documented
@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingId {
    String message() default "Id not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends CrudService<?, UUID>> responsible();
}
