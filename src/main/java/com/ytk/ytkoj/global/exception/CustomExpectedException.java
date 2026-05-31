package com.ytk.ytkoj.global.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public class CustomExpectedException extends RuntimeException {
    @Getter
    @Setter
    private Level level; // 로그 레벨

    @Getter
    @Setter
    private HttpStatus httpStatus; // 클라이언트에 보여줄 서버 상태 코드

    /**
     * 로그 레벨, http 상태 코드 모두 수정이 필요할 때 사용하는 생성자입니다.
     * */
    public CustomExpectedException(Level level, HttpStatus httpStatus, String message) {
        super(message);
        this.level = level;
        this.httpStatus = httpStatus;
    }

    /**
     * http 상태 코드만 수정이 필요할 때 사용합니다. 로그 레벨은 INFO으로 고정됩니다.
     * */
    public CustomExpectedException(HttpStatus httpStatus, String message) {
        this(Level.INFO, httpStatus, message);
    }

    /**
     * 예외 메시지를 발생시킵니다. 기본 로그 메시지 레벨은 INFO, HttpStatus는 500으로 설정합니다.
     * */
    public CustomExpectedException(String message){
        this(Level.INFO, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
