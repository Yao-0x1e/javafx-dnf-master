package com.garena.dnfmaster.util;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceUtils {
    public static DataSource buildDataSource(String host, int port, String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        String url = String.format("jdbc:mysql://%s:%d?characterEncoding=UTF-8&sslMode=PREFERRED", host, port);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxConnLifetimeMillis(1800000L);
        dataSource.setMaxWaitMillis(1000L);
        return dataSource;
    }
}
