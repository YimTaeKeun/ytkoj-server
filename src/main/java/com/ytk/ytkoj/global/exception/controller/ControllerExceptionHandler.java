package com.ytk.ytkoj.global.exception.controller;

import com.ytk.ytkoj.global.exception.CustomExpectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    private final Environment environment;

    /**
     * 미리 정의된 예외에 의한 컨트롤러
     * */
    @ExceptionHandler(CustomExpectedException.class)
    public ResponseEntity<ExceptionDTO> handleCustomExpectedException(CustomExpectedException e){
        ExceptionDTO dto = buildDto(e);
        return ResponseEntity.status(e.getHttpStatus()).body(dto);
    }

    /**
     * 유저를 찾을 수 없을 때
     * */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUsernameNotFound(UsernameNotFoundException e){
        ExceptionDTO dto = buildDto(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    /**
     * 예상치 못한 예외
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleException(Exception e){
        ExceptionDTO dto = buildDto(e);
        log.error("Exception: {}", e.getMessage());

        if(hasProfile("dev")) log.error(Arrays.toString(e.getStackTrace()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    private ExceptionDTO buildDto(Exception ex) {
        return ExceptionDTO.builder()
                .message(ex.getMessage())
                .build();
    }

    private Boolean hasProfile(String profile){
        String[] activeProfiles = environment.getActiveProfiles();
        for(String each: activeProfiles) if(each.equals(profile)) return true;
        return false;
    }
}
