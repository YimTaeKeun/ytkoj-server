package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomExpectedException {
    public UnAuthorizedException(String message) {
        super(
                Level.INFO,
                HttpStatus.UNAUTHORIZED,
                message
        );
    }
}
