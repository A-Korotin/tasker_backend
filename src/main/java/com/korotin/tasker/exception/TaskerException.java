package com.korotin.tasker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TaskerException extends RuntimeException {

    private HttpStatus responseStatus;

    public TaskerException() {
    }

    public TaskerException(String msg) {
        super(msg);
    }

    public TaskerException(String message, HttpStatus status) {
        super(message);
        this.responseStatus = status;
    }

}
