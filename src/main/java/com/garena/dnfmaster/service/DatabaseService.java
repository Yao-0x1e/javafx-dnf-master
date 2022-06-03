package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.common.AppRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;

@Component
@Slf4j
public class DatabaseService {
    @Autowired
    private BasicDataSource dataSource;

    public DatabaseService() {
        AppRegistry.putValue("isDatabaseConnected", false);
    }

    private boolean isConnected() {
        return AppRegistry.getValue("isDatabaseConnected", Boolean.class);
    }

    @SneakyThrows
    public void connect(String username, String password, String host, String port) {
        Assert.isFalse(isConnected(), "请勿重复进行数据库连接操作！");
        Assert.notEmpty(username, "数据库用户不能为空");
        Assert.notEmpty(password, "数据库密码不能为空");
        Assert.notEmpty(host, "数据库地址不能为空");
        Assert.checkBetween(Integer.parseInt(port), 1, 65535, "数据库端口不在合法范围内");

        String dataSourceUrl = String.format("jdbc:mysql://%s:%s?characterEncoding=UTF-8&sslMode=PREFERRED", host, port);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
        log.info(databaseMetaData.toString());
        AppRegistry.putValue("isDatabaseConnected", true);
    }

    @SneakyThrows
    public void disconnect() {
        Assert.isTrue(isConnected(), "当前仍未连接至任何数据库！");

        dataSource.getConnection().close();
        AppRegistry.putValue("isDatabaseConnected", false);
    }
}
