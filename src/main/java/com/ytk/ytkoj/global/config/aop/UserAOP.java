package com.ytk.ytkoj.global.config.aop;

import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.service.UserServiceDeprecated;
import com.ytk.ytkoj.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Aspect
@RequiredArgsConstructor
public class UserAOP {

    private final UserServiceDeprecated userServiceDeprecated;

    /**
     * 핸들러가 있는지 검사합니다.
     * */
    @Before("handlerCheckAnnotation()")
    public void handlerCheck(JoinPoint joinPoint){
        UserJpaEntity userJpaEntity = userServiceDeprecated.authenticateUser();
        if(userJpaEntity.getHandle() == null){
            log.warn("핸들없는 유저의 요청 발생");
            throw new BadRequestException("NO HANDLE USER");
        }
    }

    @Pointcut("@annotation(com.ytk.ytkoj.global.config.aop.UserHandlerCheck)")
    public void handlerCheckAnnotation(){}
}
