package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.service.AccountService;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.DialogUtils;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;

public class AccountPanelController {
    @FXML
    private MFXTextField accountUsernameField;
    @FXML
    private MFXPasswordField accountPasswordField;

    private final AccountService accountService;

    public AccountPanelController() {
        accountService = AppContextUtils.getBean(AccountService.class);
        AppContextUtils.addBean(AccountPanelController.class, this);
    }

    public void onLoginButtonClicked() throws Throwable {
        String accountName = accountUsernameField.getText();
        String password = accountPasswordField.getText();
        accountService.login(accountName, password);
    }

    public void onRegisterButtonClicked() {
        String accountName = accountUsernameField.getText();
        String password = accountPasswordField.getText();
        accountService.register(accountName, password);
    }

    public void onPasswordChangeButtonClicked() {
        String accountName = DialogUtils.showInputDialogAndWait("修改密码", "请输入账号", "账号：");
        String oldPassword = DialogUtils.showInputDialogAndWait("修改密码", "请输入原密码", "原密码：");
        String newPassword = DialogUtils.showInputDialogAndWait("修改密码", "请输入新密码", "新密码：");
        accountService.changePassword(accountName, oldPassword, newPassword);
    }

    public void onCharacterRefreshButtonClicked() {
        String accountName = accountUsernameField.getText();
        accountService.refreshCharacters(accountName);
    }
}
