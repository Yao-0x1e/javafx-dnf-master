package com.garena.dnfmaster.controller;

import cn.hutool.core.lang.Assert;
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
    }

    public void onLoginButtonClicked() {
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
        String accountName = DialogUtils.showInputDialog("修改密码", "请输入账号", "账号：");
        Assert.notNull(accountName);
        String oldPassword = DialogUtils.showInputDialog("修改密码", "请输入原密码", "原密码：");
        Assert.notNull(oldPassword);
        String newPassword = DialogUtils.showInputDialog("修改密码", "请输入新密码", "新密码：");
        Assert.notNull(newPassword);

        accountService.changePassword(accountName, oldPassword, newPassword);
    }

    public void onCharacterRefreshButtonClicked() {
        String accountName = accountUsernameField.getText();
        accountService.refreshCharacters(accountName);
    }
}
