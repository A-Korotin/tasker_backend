package com.korotin.tasker.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class NotFoundException extends TaskerException {

    public NotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
