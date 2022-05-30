package com.garena.dnfmaster.service;

import com.garena.dnfmaster.util.DialogUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DatabaseService {
    @Autowired
    private BasicDataSource dataSource;

    public void connect(String username, String password, String host, int port) throws SQLException {
        String dataSourceUrl = String.format("jdbc:mysql://%s:%d?characterEncoding=UTF-8&sslMode=PREFERRED", host, port);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.getConnection().getMetaData();
        DialogUtils.showInfo("数据库操作", "成功连接数据库");
    }

    public void disconnect() throws SQLException {
        dataSource.close();
        dataSource.setUrl(null);
        dataSource.setUsername(null);
        dataSource.setPassword(null);
        DialogUtils.showInfo("数据库操作", "成功断开数据库");
    }
}
