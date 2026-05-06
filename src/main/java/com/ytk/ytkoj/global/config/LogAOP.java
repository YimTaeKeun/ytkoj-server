package com.ytk.ytkoj.global.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Aspect
@Slf4j
public class LogAOP {

    @Before("servicePointcut()")
    public void serviceLogging(JoinPoint joinPoint){
        log.debug("Executed Service Class: {}", joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Executed Service Method: {}, Args: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around("servicePointcut()")
    public Object serviceAround(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        // 실행 시작
        Object proceed = joinPoint.proceed();
        // 실행 종료
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        log.info("Executed ({}) in {} ms", joinPoint.getSignature().getDeclaringTypeName(), executeTime);
        return proceed;
    }



    @Pointcut("@annotation(org.springframework.stereotype.Service)")
    public void servicePointcut() {}

}
