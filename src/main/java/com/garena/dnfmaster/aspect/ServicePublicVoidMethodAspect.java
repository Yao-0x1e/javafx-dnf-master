package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.common.AppRegistry;
import com.garena.dnfmaster.dialog.DialogBuilder;
import com.garena.dnfmaster.dialog.NotificationDialog;
import com.garena.dnfmaster.dialog.WaitingDialog;
import com.garena.dnfmaster.service.DatabaseService;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.AsyncTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

// Order的数值越小优先级越高
// 优先级越高会在越外层被执行，也就是会越后面织入
// 相同优先级的切面会按名称排序然后织入
@Order(value = 1)
@Aspect
@Configuration
public class ServicePublicVoidMethodAspect {
    // 不能在切面注入或者获取切点所包含的对象
    // 在运行时可能会出现重复织入的问题
    @Pointcut("execution(public void com.garena.dnfmaster.service.*Service.*(..))")
    public void executionPointcut() {
    }

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private WaitingDialog waitingDialog = null;
    // private GenericDialog infoDialog = null;
    // private GenericDialog errorDialog = null;
    // private GenericDialog warningDialog = null;
    private NotificationDialog infoDialog = null;
    private NotificationDialog errorDialog = null;
    private NotificationDialog warningDialog = null;

    private void initDialogs() {
        Stage primaryStage = AppContext.getBean(Stage.class);
        waitingDialog = DialogBuilder.buildProgressBarWaitingDialog(primaryStage, "请耐心等待操作执行完成...");

        // infoDialog = DialogBuilder.buildInfoGenericDialog(primaryStage, "", "");
        // errorDialog = DialogBuilder.buildErrorGenericDialog(primaryStage, "", "");
        // warningDialog = DialogBuilder.buildWarningGenericDialog(primaryStage, "", "");

        infoDialog = DialogBuilder.buildInfoNotificationDialog("", "");
        errorDialog = DialogBuilder.buildErrorNotificationDialog("", "");
        warningDialog = DialogBuilder.buildWarningNotificationDialog("", "");
    }

    private boolean isMethodAllowed(ProceedingJoinPoint proceedingJoinPoint) {
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 除了数据库连接函数之外的所有Service函数都必须在连接数据库后调用
        boolean isDatabaseConnected = AppRegistry.getValue("isDatabaseConnected", Boolean.class);
        if (!isDatabaseConnected) {
            // 数据库未连接的情况
            // 判断是否为数据库连接函数，如果是则放行
            String targetMethodName = method.getName();
            return DatabaseService.class.equals(targetClass) && "connect".equals(targetMethodName);
        }
        return true;
    }

    @Around(value = "executionPointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        if (waitingDialog == null) {
            initDialogs();
        }
        if (!isMethodAllowed(proceedingJoinPoint)) {
            warningDialog.show("非法操作", "请连接数据库后在进行操作！");
            return null;
        }

        reentrantLock.lock();
        waitingDialog.show();
        asyncTaskExecutor.submit(() -> {
            try {
                proceedingJoinPoint.proceed();
                Platform.runLater(() -> {
                    waitingDialog.close();
                    infoDialog.show("消息", "成功执行目标操作");
                });
            } catch (Throwable throwable) {
                Throwable cause = throwable.getCause();
                String message = cause == null ? throwable.toString() : cause.toString();
                Platform.runLater(() -> {
                    waitingDialog.close();
                    errorDialog.show("错误", message);
                });
            } finally {
                reentrantLock.unlock();
            }
        });
        return null;
    }
}
