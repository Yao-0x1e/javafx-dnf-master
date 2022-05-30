package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.service.DatabaseService;
import com.garena.dnfmaster.util.AppContextUtils;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;

public class DatabasePanelController {
    @FXML
    private MFXTextField hostField;
    @FXML
    private MFXTextField portField;
    @FXML
    private MFXTextField usernameField;
    @FXML
    private MFXPasswordField passwordField;

    private final DatabaseService databaseService;

    public DatabasePanelController() {
        databaseService = AppContextUtils.getBean(DatabaseService.class);
    }

    public void onConnectButtonClicked() {
    }

    public void onDisconnectButtonClicked() {
    }
}
