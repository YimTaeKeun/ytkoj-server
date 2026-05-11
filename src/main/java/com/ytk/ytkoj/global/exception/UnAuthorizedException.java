package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * HttpStatus: 401 UNAUTHORIZED
 * */
public class UnAuthorizedException extends CustomExpectedException {

    /**
     * HttpStatus: 401 UNAUTHORIZED
     * */
    public UnAuthorizedException(String message) {
        super(
                Level.INFO,
                HttpStatus.UNAUTHORIZED,
                message
        );
    }
}
