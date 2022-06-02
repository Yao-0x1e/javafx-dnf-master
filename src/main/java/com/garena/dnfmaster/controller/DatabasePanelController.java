package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.service.DatabaseService;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

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
        databaseService = AppContext.getBean(DatabaseService.class);
    }


    @SneakyThrows
    public synchronized void onConnectButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String host = hostField.getText();
        String port = portField.getText();
        databaseService.connect(username, password, host, port);
    }

    @SneakyThrows
    public synchronized void onDisconnectButtonClicked() {
        databaseService.disconnect();
    }
}
