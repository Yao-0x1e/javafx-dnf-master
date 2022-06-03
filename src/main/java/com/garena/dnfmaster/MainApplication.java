package com.garena.dnfmaster;

import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.controller.MainPanelController;
import com.garena.dnfmaster.util.ResourceUtils;
import fr.brouillard.oss.cssfx.CSSFX;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MainApplication extends Application {
    private void destroyAsyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = (ThreadPoolTaskExecutor) AppContext.getBean(AsyncTaskExecutor.class);
        asyncTaskExecutor.destroy();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext.addBean(Stage.class, primaryStage);
        MFXNotificationSystem.instance().initOwner(primaryStage);

        CSSFX.start();
        FXMLLoader loader = new FXMLLoader(ResourceUtils.loadURL("/fxml/MainPanel.fxml"));
        loader.setControllerFactory(c -> new MainPanelController(primaryStage));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DNF Master");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        destroyAsyncTaskExecutor();
    }
}
