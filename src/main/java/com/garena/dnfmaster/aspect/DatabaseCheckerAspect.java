package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.service.DatabaseService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Order(value = 2)
@Aspect
@Configuration
public class DatabaseCheckerAspect {
    @Autowired
    private DatabaseService databaseService;

    @Pointcut("@annotation(com.garena.dnfmaster.annotation.DatabaseRequired))")
    private void annotationPointcut() {
    }

    @Around(value = "annotationPointcut()")
    public Object executeOptionally(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (databaseService.isConnected()) {
            return proceedingJoinPoint.proceed();
        } else {
            throw new Exception("请先连接数据库再进行操作");
        }
    }
}
