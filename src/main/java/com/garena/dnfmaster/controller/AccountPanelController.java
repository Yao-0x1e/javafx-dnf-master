package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.service.AccountService;
import com.garena.dnfmaster.util.AppContextUtils;
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
    }

    public void onRegisterButtonClicked() {
    }

    public void onPasswordChangeButtonClicked() {
    }

    public void onCharacterRefreshButtonClicked() {
    }
}
