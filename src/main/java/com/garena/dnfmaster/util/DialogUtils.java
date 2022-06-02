package com.garena.dnfmaster.util;

import com.garena.dnfmaster.dialog.WaitingDialog;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DialogUtils {
    private static final List<String> styleClasses = Arrays.asList("mfx-info-dialog", "mfx-warn-dialog", "mfx-error-dialog");
    private static final MFXGenericDialog mfxGenericDialog;
    private static final MFXStageDialog mfxStageDialog;

    static {
        mfxGenericDialog = MFXGenericDialogBuilder.build()
                .makeScrollable(true)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setShowClose(false)
                .get();
        mfxStageDialog = MFXGenericDialogBuilder.build(mfxGenericDialog)
                .toStageDialogBuilder()
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setScrimPriority(ScrimPriority.NODE)
                .setScrimOwner(true)
                .get();

        mfxGenericDialog.addActions(Map.entry(new MFXButton("确定"), event -> mfxStageDialog.close()));
    }

    private static void changeDialogStyleClass(String styleClass) {
        mfxGenericDialog.getStyleClass().removeIf(styleClasses::contains);
        if (styleClass != null) {
            mfxGenericDialog.getStyleClass().add(styleClass);
        }
    }

    private static void showDialog(MFXFontIcon headerIcon, String styleClass, String headerText, String contentText) {
        mfxGenericDialog.setHeaderIcon(headerIcon);
        mfxGenericDialog.setHeaderText(headerText);
        mfxGenericDialog.setContentText(contentText);
        changeDialogStyleClass(styleClass);
        mfxStageDialog.showDialog();
    }

    public static void showInfo(String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-info-circle", 18);
        showDialog(icon, styleClasses.get(0), title, message);
    }

    public static void showWarning(String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        showDialog(icon, styleClasses.get(1), title, message);
    }

    public static void showError(String title, String message) {
        MFXFontIcon icon = new MFXFontIcon("mfx-exclamation-circle-filled", 18);
        showDialog(icon, styleClasses.get(2), title, message);
    }

    public static void showGeneric(String title, String message) {
        showDialog(null, null, title, message);
    }

    public static boolean showConfirmationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType confirmButton = new ButtonType("确认", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("取消", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }

    public static String showInputDialog(String title, String headerText, String label) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(label);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static String showChoiceDialog(String title, String headerText, String label, List<String> options) {
        assert options != null && !options.isEmpty();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(label);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
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
}
