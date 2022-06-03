package com.garena.dnfmaster.dialog;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.util.ResourceUtils;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

public class DialogBuilder {
    public static GenericDialog buildInfoGenericDialog(Stage owner, String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-info-circle", 18);
        return new GenericDialog(owner, icon, "mfx-info-dialog", title, message);
    }

    public static GenericDialog buildWarningGenericDialog(Stage owner, String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        return new GenericDialog(owner, icon, "mfx-warn-dialog", title, message);
    }

    public static GenericDialog buildErrorGenericDialog(Stage owner, String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-exclamation-circle-filled", 18);
        return new GenericDialog(owner, icon, "mfx-error-dialog", title, message);
    }

    public static Alert buildYesOrNoDialog(Stage owner, String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initOwner(owner);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        return alert;
    }

    public static TextInputDialog buildTextInputDialog(Stage owner, String title, String headerText, String label) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.initOwner(owner);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(label);
        return dialog;
    }

    public static ChoiceDialog<String> buildChoiceDialog(Stage owner, String title, String headerText, String label, List<String> options) {
        Assert.notEmpty(options, "选项列表不能为空");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.initOwner(owner);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(label);
        dialog.setWidth(200);
        return dialog;
    }

    public static WaitingDialog buildAnimationWaitingDialog(Stage owner, String message) {
        InputStream inputStream = ResourceUtils.loadStream("/pic/waiting.gif");
        Image waitingImage = new Image(inputStream);
        ImageView waitingView = new ImageView(waitingImage);
        waitingView.setFitHeight(owner.getHeight());
        waitingView.setFitWidth(owner.getWidth());

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font(15));

        StackPane root = new StackPane();
        root.getChildren().addAll(waitingView, messageLabel);
        return new WaitingDialog(owner, root);
    }

    public static WaitingDialog buildProgressBarWaitingDialog(Stage owner, String message) {
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font(15));

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(messageLabel, new MFXProgressBar());
        return new WaitingDialog(owner, root);
    }

    public static WaitingDialog buildProgressSpinnerWaitingDialog(Stage owner, String message) {
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font(15));

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(new MFXProgressSpinner(), messageLabel);
        return new WaitingDialog(owner, root);
    }

    public static NotificationDialog buildInfoNotificationDialog(String title, String message) {
        return new NotificationDialog("mfx-bell", title, message);
    }

    public static NotificationDialog buildWarningNotificationDialog(String title, String message) {
        return new NotificationDialog("mfx-do-not-enter-circle", title, message);
    }

    public static NotificationDialog buildErrorNotificationDialog(String title, String message) {
        return new NotificationDialog("mfx-x-circle", title, message);
    }
}
