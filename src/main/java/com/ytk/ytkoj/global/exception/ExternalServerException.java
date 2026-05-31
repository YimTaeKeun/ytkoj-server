package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * 우리 서비스 서버가 아닌 외부 API 서버의 오류라고 판단했을 경우
 * */
public class ExternalServerException extends CustomExpectedException{
    public ExternalServerException(String message) {
        super(
                Level.WARN,
                HttpStatus.INTERNAL_SERVER_ERROR,
                message
        );
    }
}
