package com.garena.dnfmaster.service;

import com.garena.dnfmaster.util.DataSourceUtils;
import com.garena.dnfmaster.util.DialogUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void connect(String username, String password, String host, int port) throws SQLException {
        assert jdbcTemplate.getDataSource() instanceof MysqlDataSource;
        DataSource dataSource = DataSourceUtils.buildDataSource(host, port, username, password);
        dataSource.getConnection().getSchema();
        jdbcTemplate.setDataSource(dataSource);
        DialogUtils.showInfo("数据库连接", "成功连接至数据库：" + dataSource.getConnection().getCatalog());
    }

    public void disconnect() throws SQLException {
        DataSource dataSource = jdbcTemplate.getDataSource();
        assert dataSource != null;
        Connection connection = dataSource.getConnection();
        assert connection != null;
        connection.close();
        jdbcTemplate.setDataSource(new MysqlDataSource());
        DialogUtils.showInfo("数据库连接", "成功断开数据库连接：" + connection.getCatalog());
    }
}
