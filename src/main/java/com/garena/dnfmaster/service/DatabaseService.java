package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.util.DialogUtils;
import lombok.SneakyThrows;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    @Autowired
    private BasicDataSource dataSource;
    private boolean isConnected = false;

    @SneakyThrows
    public void connect(String username, String password, String host, String port) {
        Assert.isFalse(isConnected, "请勿重复进行数据库连接操作！");
        Assert.notEmpty(username, "数据库用户不能为空");
        Assert.notEmpty(password, "数据库密码不能为空");
        Assert.notEmpty(host, "数据库地址不能为空");
        Assert.checkBetween(Integer.parseInt(port), 1, 65535, "数据库端口不在合法范围内");

        String dataSourceUrl = String.format("jdbc:mysql://%s:%s?characterEncoding=UTF-8&sslMode=PREFERRED", host, port);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        DialogUtils.showInfo("数据库操作", "成功连接数据库：" + dataSource.getConnection().getMetaData());
        isConnected = true;
    }

    @SneakyThrows
    public void disconnect() {
        Assert.isTrue(isConnected, "当前仍未连接至任何数据库！");

        dataSource.getConnection().close();
        DialogUtils.showInfo("数据库操作", "成功断开数据库");
        isConnected = false;
    }
}