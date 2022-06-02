package com.garena.dnfmaster.dialog;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WaitingDialog {
    private final Stage stage;

    public WaitingDialog(Stage owner, Pane root) {
        root.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0), null, null)));
        root.setMouseTransparent(true);
        root.setPrefSize(owner.getWidth(), owner.getHeight());

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(owner);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().addAll(owner.getIcons());
        stage.setX(owner.getX());
        stage.setY(owner.getY());
        stage.setHeight(owner.getHeight());
        stage.setWidth(owner.getWidth());
    }

    public void show() {
        Platform.runLater(stage::show);
    }

    public void close() {
        Platform.runLater(stage::close);
    }
}
