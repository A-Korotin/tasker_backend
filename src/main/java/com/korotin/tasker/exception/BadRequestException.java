package com.korotin.tasker.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends TaskerException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
