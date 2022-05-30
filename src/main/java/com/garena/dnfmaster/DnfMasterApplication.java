package com.garena.dnfmaster;

import com.garena.dnfmaster.util.AppContextUtils;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class DnfMasterApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DnfMasterApplication.class, args);
        AppContextUtils.setApplicationContext(applicationContext);
        Application.launch(MainApplication.class, args);
    }
}
