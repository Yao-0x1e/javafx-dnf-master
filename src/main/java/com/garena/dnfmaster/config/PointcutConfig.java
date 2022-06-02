package com.garena.dnfmaster.config;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointcutConfig {
    @Pointcut("execution(public * com.garena.dnfmaster.service.*Service.*(..))")
    public void servicePointcut() {
    }

    @Pointcut("@annotation(com.garena.dnfmaster.annotation.ExceptionHandler)")
    public void exceptionHandlerPointcut() {

    }
}
