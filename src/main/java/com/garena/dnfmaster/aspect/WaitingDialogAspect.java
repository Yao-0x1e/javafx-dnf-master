package com.garena.dnfmaster.aspect;

import com.garena.dnfmaster.dialog.WaitingDialog;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.DialogUtils;
import javafx.stage.Stage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// 优先级越高会在越外层被执行，也就是会越后面织入
@Order(value = 128)
@Aspect
@Configuration
public class WaitingDialogAspect {
    private WaitingDialog waitingDialog = null;

    private void initWaitingDialog() {
        Stage primaryStage = AppContextUtils.getBean(Stage.class);
        waitingDialog = DialogUtils.buildProgressSpinnerWaitingDialog(primaryStage, "请耐心等待操作执行完成...");
    }

    @Around(value = "com.garena.dnfmaster.config.PointcutConfig.exceptionHandlerPointcut()")
    public Object showWaitingDialog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (waitingDialog == null) {
            initWaitingDialog();
        }

        try {
            waitingDialog.show();
            return proceedingJoinPoint.proceed();
        } finally {
            waitingDialog.close();
        }
    }
}
