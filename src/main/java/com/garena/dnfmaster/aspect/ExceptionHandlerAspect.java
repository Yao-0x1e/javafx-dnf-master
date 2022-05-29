package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.util.DialogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlerAspect {
    @Pointcut("execution(public * com.garena.dnfmaster.service.*Service.*(..))")
    private void executionPointcut() {

    }

    @Pointcut("@annotation(com.garena.dnfmaster.annotation.ExceptionHandler))")
    private void annotationPointCut() {
    }

    @AfterThrowing(pointcut = "executionPointcut()", throwing = "throwable")
    public void around(JoinPoint joinPoint, Throwable throwable) {
        DialogUtils.showError("错误", throwable.toString());
    }
}
