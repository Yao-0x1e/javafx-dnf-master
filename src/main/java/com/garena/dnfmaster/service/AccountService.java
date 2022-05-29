package com.garena.dnfmaster.service;

import com.garena.dnfmaster.controller.AdminPanelController;
import com.garena.dnfmaster.dao.AccountDao;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.CommandUtils;
import com.garena.dnfmaster.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    public void register(String accountName, String password) {
        Integer uid = accountDao.findUidByAccountName(accountName);
        accountDao.createAccount(accountName, password);
    }

    public void login(String accountName, String password) throws Throwable {
        Integer uid = accountDao.findUidByAccountNameAndPassword(accountName, password);
        String token = SecurityUtils.getToken(uid);
        CommandUtils.exec("DNF.exe", token);
    }

    public void changePassword(String accountName, String oldPassword, String newPassword) {
        Integer uid = accountDao.findUidByAccountNameAndPassword(accountName, oldPassword);
        accountDao.setPassword(accountName, newPassword);
    }

    public void refreshCharacters(String accountName) {
        Integer uid = accountDao.findUidByAccountName(accountName);
        List<Charac> characters = accountDao.findCharacters(uid);
        AdminPanelController adminPanelController = AppContextUtils.getBean(AdminPanelController.class);
        adminPanelController.setCharacters(characters);
    }
}
