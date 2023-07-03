package com.korotin.tasker.validator.annotation;

import com.korotin.tasker.validator.EditUserEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation ensuring unique {@link com.korotin.tasker.domain.User}
 * email constraint when account is being edited.
 * Checks if provided email is unique or is equal to the previous one.
 */
@Documented
@Constraint(validatedBy = EditUserEmailValidator.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserEmail {
    String message() default "Email is not unique (and does not equal previous)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int dtoIndex() default 0;
    int idIndex() default 1;
}
