package com.ytk.ytkoj.global.config.aop;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.service.internal.UserInternalService;
import com.ytk.ytkoj.global.exception.BadRequestException;
import com.ytk.ytkoj.global.exception.ForbiddenException;
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

    private final UserInternalService userService;

    /**
     * 핸들러가 있는지 검사합니다.
     * */
    @Before("handlerCheckAnnotation()")
    public void handlerCheck(JoinPoint joinPoint){
        User user = userService.getAuthenticateUser();
        if(user.getHandle() == null){
            log.warn("핸들없는 유저의 요청 발생");
            throw new BadRequestException("NO HANDLE USER");
        }
    }

    @Before("adminOnlyAnnotation()")
    public void adminOnly(JoinPoint joinPoint){
        User user = userService.getAuthenticateUser();
        if(!user.isAdmin()){
            log.warn("관리자 외 접근 시도");
            throw new ForbiddenException("허용되지 않은 접근");
        }
    }

    @Pointcut("@annotation(com.ytk.ytkoj.global.config.aop.UserHandlerCheck)")
    public void handlerCheckAnnotation(){}

    @Pointcut("@annotation(com.ytk.ytkoj.global.config.aop.AdminOnly)")
    public void adminOnlyAnnotation(){}
}
