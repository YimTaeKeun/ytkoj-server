package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * 사용자가 요청을 잘못 보냈을 경우
 * */
public class BadRequestException extends CustomExpectedException{
    public BadRequestException(String message) {
        super(
                Level.INFO,
                HttpStatus.BAD_REQUEST,
                message
        );
    }
}
