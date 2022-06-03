package com.garena.dnfmaster.dialog;

import com.garena.dnfmaster.util.ResourceUtils;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class NotificationDialog extends MFXSimpleNotification {
    private final StringProperty headerText = new SimpleStringProperty();
    private final StringProperty contentText = new SimpleStringProperty();

    public NotificationDialog(String iconDescription, String headerText, String contentText) {
        MFXIconWrapper icon = new MFXIconWrapper(iconDescription, 16, 32);

        Label headerLabel = new Label();
        setHeaderText(headerText);
        headerLabel.textProperty().bind(this.headerText);
        StackPane placeHolder = new StackPane();
        placeHolder.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(placeHolder, Priority.ALWAYS);
        HBox header = new HBox(10, icon, headerLabel, placeHolder);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(InsetsFactory.of(5, 0, 5, 0));
        header.setMaxWidth(Double.MAX_VALUE);

        setContentText(contentText);
        Label contentLabel = new Label();
        contentLabel.getStyleClass().add("content");
        contentLabel.textProperty().bind(this.contentText);
        contentLabel.setWrapText(true);
        contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        contentLabel.setAlignment(Pos.TOP_LEFT);

        BorderPane container = new BorderPane();
        container.getStyleClass().add("notification");
        container.setTop(header);
        container.setCenter(contentLabel);
        container.getStylesheets().add(ResourceUtils.load("/css/Notifications.css"));

        setContent(container);
    }

    public void show(String headerText, String contentText) {
        setHeaderText(headerText);
        setContentText(contentText);
        MFXNotificationSystem.instance()
                .setPosition(NotificationPos.TOP_CENTER)
                .publish(this);
    }

    public String getHeaderText() {
        return headerText.get();
    }

    public void setHeaderText(String headerText) {
        this.headerText.set(headerText);
    }

    public String getContentText() {
        return contentText.get();
    }

    public void setContentText(String contentText) {
        this.contentText.set(contentText);
    }
}
