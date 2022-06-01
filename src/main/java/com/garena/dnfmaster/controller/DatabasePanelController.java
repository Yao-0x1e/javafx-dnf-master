package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.service.DatabaseService;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.DialogUtils;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

import java.sql.DatabaseMetaData;

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


    @SneakyThrows
    public void onConnectButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String host = hostField.getText();
        String port = portField.getText();
        DatabaseMetaData databaseMetaData = databaseService.connect(username, password, host, port);
        DialogUtils.showInfo("数据库操作", "成功连接数据库：" + databaseMetaData);
    }

    @SneakyThrows
    public void onDisconnectButtonClicked() {
        databaseService.disconnect();
        DialogUtils.showInfo("数据库操作", "成功断开数据库");
    }
}
