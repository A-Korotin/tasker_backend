package com.korotin.tasker.validator;

import com.korotin.tasker.domain.Record;
import com.korotin.tasker.exception.NotFoundException;
import com.korotin.tasker.service.CrudService;
import com.korotin.tasker.service.ProjectService;
import com.korotin.tasker.validator.annotation.ValidRecord;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ProjectRecordValidator implements ConstraintValidator<ValidRecord, Object[]> {
    private final ProjectService projectService;
    private final ApplicationContext context;
    private int recordIndex,
                projectIndex;

    private CrudService<? extends Record, UUID> recordProvider;

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (!(value[projectIndex] instanceof UUID projectId) || !(value[recordIndex] instanceof UUID recordId)) {
            throw new IllegalArgumentException("Method signature does not match expected format");
        }

        Record record = recordProvider.findById(recordId).orElseThrow(() ->
                new NotFoundException("Record with id '%s' does not exist".formatted(recordId)));

        if (record.getProject().getId().equals(projectId)) {
            return true;
        }

        throw new NotFoundException("Record '%s' does not belong to the project '%s'"
                .formatted(recordId, projectId));
    }

    @Override
    public void initialize(ValidRecord constraintAnnotation) {
        this.projectIndex = constraintAnnotation.projectIndex();
        this.recordIndex = constraintAnnotation.recordIndex();

        var provider = constraintAnnotation.recordProvider();
        this.recordProvider = this.context.getBean(provider);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
