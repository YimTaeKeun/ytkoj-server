package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomExpectedException {
    public InternalServerException(String message) {
        super(
                Level.ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                message
        );
    }
}
