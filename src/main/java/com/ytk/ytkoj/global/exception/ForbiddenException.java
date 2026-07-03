package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * 사용자가 요청을 잘못 보냈을 경우
 * */
public class ForbiddenException extends CustomExpectedException{
    public ForbiddenException(String message) {
        super(
                Level.INFO,
                HttpStatus.FORBIDDEN,
                message
        );
    }
}
