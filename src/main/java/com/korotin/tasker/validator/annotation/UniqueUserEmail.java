package com.korotin.tasker.validator.annotation;

import com.korotin.tasker.validator.UserEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation ensuring unique {@link com.korotin.tasker.domain.User} email constraint
 */
@Documented
@Constraint(validatedBy = UserEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {
    String message() default "Non unique email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

