package com.garena.dnfmaster.dialog;

import cn.hutool.core.lang.Assert;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GenericDialog {
    private final MFXGenericDialog mfxGenericDialog;
    private final MFXStageDialog mfxStageDialog;

    public GenericDialog(Stage owner, MFXFontIcon headerIcon, String styleClass, String headerText, String contentText) {
        List<String> styleClasses = Arrays.asList("mfx-info-dialog", "mfx-warn-dialog", "mfx-error-dialog");
        Assert.isTrue(styleClasses.contains(styleClass), "不支持该属性");

        mfxGenericDialog = MFXGenericDialogBuilder.build()
                .makeScrollable(true)
                .setShowAlwaysOnTop(false)
                .setShowMinimize(false)
                .setShowClose(false)
                .get();
        mfxStageDialog = MFXGenericDialogBuilder.build(mfxGenericDialog)
                .toStageDialogBuilder()
                .initOwner(owner)
                .setDraggable(true)
                .setScrimPriority(ScrimPriority.NODE)
                .setScrimOwner(true)
                .get();

        mfxGenericDialog.addActions(Map.entry(new MFXButton("确定"), event -> mfxStageDialog.close()));
        mfxGenericDialog.setHeaderIcon(headerIcon);
        mfxGenericDialog.setHeaderText(headerText);
        mfxGenericDialog.setContentText(contentText);
        mfxGenericDialog.getStyleClass().add(styleClass);
    }

    public void show() {
        mfxStageDialog.showDialog();
    }

    public void show(String headerText, String contentText) {
        setHeaderText(headerText);
        setContentText(contentText);
        mfxStageDialog.showDialog();
    }

    public void close() {
        mfxStageDialog.close();
    }

    public String getHeaderText() {
        return mfxGenericDialog.getHeaderText();
    }

    public void setHeaderText(String headerText) {
        mfxGenericDialog.setHeaderText(headerText);
    }

    public String getContentText() {
        return mfxGenericDialog.getContentText();
    }

    public void setContentText(String contentText) {
        mfxGenericDialog.setContentText(contentText);
    }
}
