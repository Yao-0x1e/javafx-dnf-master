package com.garena.dnfmaster;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DnfMasterApplication {

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }

}
