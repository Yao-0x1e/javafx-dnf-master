package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.util.DialogUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Order(value = 1)
@Aspect
@Configuration
public class ExceptionHandlerAspect {
    @Pointcut("execution(public * com.garena.dnfmaster.service.*Service.*(..))")
    private void executionPointcut() {
    }

    @AfterThrowing(pointcut = "executionPointcut()", throwing = "throwable")
    public void handleException(Throwable throwable) {
        Throwable cause = throwable.getCause();
        String message = cause == null ? throwable.toString() : cause.toString();
        DialogUtils.showError("错误", message);
    }
}
