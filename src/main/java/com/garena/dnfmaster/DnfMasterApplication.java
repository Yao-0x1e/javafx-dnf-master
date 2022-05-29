package com.garena.dnfmaster;

import com.garena.dnfmaster.util.AppContextUtils;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DnfMasterApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DnfMasterApplication.class, args);
        AppContextUtils.setApplicationContext(applicationContext);
        Application.launch(MainApplication.class, args);
    }
}
