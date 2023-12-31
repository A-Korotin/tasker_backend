package com.korotin.tasker.controller.advice;

import com.korotin.tasker.exception.TaskerException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class ExceptionHandlingControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> verboselyHandleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        log.warn("Invalid input: binding errors: {}", errors);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> verboselyHandleMismatchException(MethodArgumentTypeMismatchException e) {
        String requiredType = Objects.nonNull(e.getRequiredType()) ? e.getRequiredType().getSimpleName() : "unknown";
        String argumentName = e.getName();
        Map<String, String> errors = new HashMap<>(2);
        errors.put("expectedType", requiredType);
        errors.put("argument", argumentName);
        errors.put("cause", "argument type mismatch");
        return errors;
    }

    @ExceptionHandler(TaskerException.class)
    public Map<String, String> handleTaskerException(TaskerException e, HttpServletResponse response) {
        log.warn("{} occurred with cause: {}", e.getClass().getSimpleName(), e.getMessage());
        Map<String, String> error = new HashMap<>(1);
        error.put("message", e.getMessage());
        response.setStatus(e.getResponseStatus().value());
        return error;
    }
}
