package com.ytk.ytkoj.global.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * DB에 데이터가 없을 경우 발생하는 예외입니다. 사용자 요청 실수 이므로 INFO로 로그레벨을 설정합니다.
 * */
public class NoResourceException extends CustomExpectedException{
    public NoResourceException(String message) {
        super(
                Level.INFO,
                HttpStatus.NOT_FOUND,
                message
        );
    }
}
