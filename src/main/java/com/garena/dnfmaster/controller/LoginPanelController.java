package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.bean.TextFieldOption;
import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.dialog.MultipleTextInputDialog;
import com.garena.dnfmaster.service.AccountService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {
    @FXML
    private MFXTextField accountUsernameField;
    @FXML
    private MFXPasswordField accountPasswordField;
    @FXML
    private MFXButton characterRefreshButton;
    @FXML
    private MFXButton loginButton;

    private final AccountService accountService;

    // private TextInputDialog accountNameInputDialog;
    // private TextInputDialog oldPasswordInputDialog;
    // private TextInputDialog newPasswordInputDialog;
    private MultipleTextInputDialog passwordChangeDialog;

    public LoginPanelController() {
        accountService = AppContext.getBean(AccountService.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setTooltip(new Tooltip("登录后即可到账号面板查看角色信息"));
        characterRefreshButton.setTooltip(new Tooltip("刷新后即可到账号面板查看角色信息"));
        setupDialogs();
    }

    private void setupDialogs() {
        Stage primaryStage = AppContext.getBean(Stage.class);
        List<TextFieldOption> textFieldOptions = new ArrayList<>(3);
        textFieldOptions.add(new TextFieldOption("账号：", TextField.class));
        textFieldOptions.add(new TextFieldOption("原密码：", PasswordField.class));
        textFieldOptions.add(new TextFieldOption("新密码：", PasswordField.class));
        Platform.runLater(() -> {
            // accountNameInputDialog = DialogBuilder.buildTextInputDialog(primaryStage, "修改密码", "请输入账号", "账号：");
            // oldPasswordInputDialog = DialogBuilder.buildTextInputDialog(primaryStage, "修改密码", "请输入原密码", "原密码：");
            // newPasswordInputDialog = DialogBuilder.buildTextInputDialog(primaryStage, "修改密码", "请输入新密码", "新密码：");
            passwordChangeDialog = new MultipleTextInputDialog(primaryStage, "修改密码", "请输入相关的信息", textFieldOptions);
        });
    }

    public synchronized void onLoginButtonClicked() {
        String accountName = accountUsernameField.getText();
        String password = accountPasswordField.getText();
        accountService.login(accountName, password);
    }

    public synchronized void onRegisterButtonClicked() {
        String accountName = accountUsernameField.getText();
        String password = accountPasswordField.getText();
        accountService.register(accountName, password);
    }

    public synchronized void onPasswordChangeButtonClicked() {
        passwordChangeDialog.showAndWait().ifPresent(result -> {
            String accountName = result.get(0);
            String oldPassword = result.get(1);
            String newPassword = result.get(2);
            accountService.changePassword(accountName, oldPassword, newPassword);
        });
    }

    public synchronized void onCharacterRefreshButtonClicked() {
        String accountName = accountUsernameField.getText();
        accountService.refreshCharacters(accountName);
    }
}
