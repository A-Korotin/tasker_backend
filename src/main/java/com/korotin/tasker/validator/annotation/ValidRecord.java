package com.korotin.tasker.validator.annotation;

import com.korotin.tasker.domain.Project;
import com.korotin.tasker.domain.Record;
import com.korotin.tasker.service.CrudService;
import com.korotin.tasker.validator.ProjectRecordValidator;
import com.korotin.tasker.validator.UserDTOValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;
import java.util.UUID;


/**
 * Validation constraint ensuring that {@link Record} belongs to the specified {@link Project} <br/>
 *
 * {@link #recordProvider()} is a {@link CrudService} from what the {@link CrudService#findById(Object)} method
 * will be invoked in order to provide a {@link Record} with specified id. <br/>
 * {@link #recordProvider()} will be queried from {@link org.springframework.context.ApplicationContext ApplicationContext}
 * @see Component
 * @see Service
 */
@Documented
@Constraint(validatedBy = ProjectRecordValidator.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRecord {
    String message() default "Invalid record configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int recordIndex() default  1;
    int projectIndex() default 0;
    Class<? extends CrudService<? extends Record, UUID>> recordProvider();
}
