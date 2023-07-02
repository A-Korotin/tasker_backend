package com.korotin.tasker.controller.advice;

import com.korotin.tasker.exception.ConflictException;
import com.korotin.tasker.exception.NotFoundException;
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
        String requiredType = e.getRequiredType().getSimpleName();
        String argumentName = e.getName();
        Map<String, String> errors = new HashMap<>(2);
        errors.put("expectedType", requiredType);
        errors.put("argument", argumentName);
        errors.put("cause", "argument type mismatch");
        return errors;
    }

    private Map<String, String> getErrorMessageEntity(String message) {
        Map<String, String> error = new HashMap<>(1);
        error.put("message", message);
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, String> handleNotFound(NotFoundException e) {
        log.warn("Not found exception occurred with cause: {}", e.getMessage());
        return getErrorMessageEntity(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public Map<String, String> handleConflict(ConflictException e) {
        log.warn("Conflict exception occurred with cause: {}", e.getMessage());
        return getErrorMessageEntity(e.getMessage());
    }
}
