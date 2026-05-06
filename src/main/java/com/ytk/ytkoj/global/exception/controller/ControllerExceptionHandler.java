package com.ytk.ytkoj.global.exception.controller;

import com.ytk.ytkoj.global.exception.CustomExpectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /**
     * 미리 정의된 예외에 의한 컨트롤러
     * */
    @ExceptionHandler(CustomExpectedException.class)
    public ResponseEntity<ExceptionDTO> handleCustomExpectedException(CustomExpectedException e){
        ExceptionDTO dto = buildDto(e);
        return ResponseEntity.status(e.getHttpStatus()).body(dto);
    }

    /**
     * 예상치 못한 예외
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleException(Exception e){
        ExceptionDTO dto = buildDto(e);
        log.error("Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    private ExceptionDTO buildDto(Exception ex) {
        return ExceptionDTO.builder()
                .message(ex.getMessage())
                .build();
    }
}
