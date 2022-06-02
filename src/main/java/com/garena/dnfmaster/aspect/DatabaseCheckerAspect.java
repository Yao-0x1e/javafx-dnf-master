package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.registry.RuntimeRegistry;
import com.garena.dnfmaster.service.DatabaseService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Order(value = 2)
@Aspect
@Configuration
public class DatabaseCheckerAspect {
    @Around(value = "com.garena.dnfmaster.config.PointcutConfig.servicePointcut()")
    public Object executeOptionally(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("正在执行数据库状态检查...");
        // 判断是否为数据库连接函数，如果是则放行
        String targetClassName = proceedingJoinPoint.getTarget().getClass().getName();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String targetMethod = methodSignature.getMethod().getName();
        boolean isDatabaseConnectMethod = DatabaseService.class.getName().equals(targetClassName) && "connect".equals(targetMethod);

        Boolean isDatabaseConnected = RuntimeRegistry.getValue("isDatabaseConnected", Boolean.class);
        if (isDatabaseConnectMethod || isDatabaseConnected) {
            return proceedingJoinPoint.proceed();
        } else {
            throw new Exception("请先连接数据库再进行操作");
        }
    }
}
