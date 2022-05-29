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
        AppContextUtils.addBean(DatabasePanelController.class, this);
    }

    public void onConnectButtonClicked() throws Exception {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());
        String username = usernameField.getText();
        String password = passwordField.getText();
        databaseService.connect(username, password, host, port);
    }

    public void onDisconnectButtonClicked() throws Exception {
        databaseService.disconnect();
    }
}
