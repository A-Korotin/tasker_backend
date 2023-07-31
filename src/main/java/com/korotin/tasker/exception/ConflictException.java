package com.korotin.tasker.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class ConflictException extends TaskerException {
    public ConflictException(String msg) {
        super(msg, HttpStatus.CONFLICT);
    }
}
