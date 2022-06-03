package com.garena.dnfmaster.dialog;

import com.garena.dnfmaster.bean.TextFieldOption;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MultipleTextInputDialog extends Dialog<List<String>> {
    public MultipleTextInputDialog(Stage owner, String title, String headerText, List<TextFieldOption> fieldOptions) {
        initOwner(owner);

        setTitle(title);
        setHeaderText(headerText);

        ButtonType confirmButton = ButtonType.OK;
        ButtonType cancelButton = ButtonType.CANCEL;
        getDialogPane().getButtonTypes().addAll(cancelButton, confirmButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        List<TextField> textFields = new ArrayList<>(fieldOptions.size());
        for (int i = 0; i < fieldOptions.size(); i++) {
            TextFieldOption fieldOption = fieldOptions.get(i);
            String fieldName = fieldOption.getName();
            Class<?> fieldType = fieldOption.getTypeClass();
            Label label = new Label(fieldName);
            if (PasswordField.class.equals(fieldType)) {
                PasswordField passwordField = new PasswordField();
                grid.add(label, 0, i);
                grid.add(passwordField, 1, i);
                textFields.add(passwordField);
            } else {
                TextField textField = new TextField();
                grid.add(label, 0, i);
                grid.add(textField, 1, i);
                textFields.add(textField);
            }
        }
        getDialogPane().setContent(grid);

        setResultConverter(button -> {
            if (button == confirmButton) {
                List<String> result = new ArrayList<>(textFields.size());
                for (TextField textField : textFields) {
                    String inputText = textField.getText();
                    result.add(inputText);
                }
                return result;
            }
            return null;
        });
    }
}
