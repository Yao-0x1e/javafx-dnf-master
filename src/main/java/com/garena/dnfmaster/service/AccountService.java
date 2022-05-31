package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.controller.ManagementPanelController;
import com.garena.dnfmaster.mapper.*;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.CommandUtils;
import com.garena.dnfmaster.util.DialogUtils;
import com.garena.dnfmaster.util.SecurityUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private LimitCreateCharacterMapper limitCreateCharacterMapper;
    @Autowired
    private MemberInfoMapper memberInfoMapper;
    @Autowired
    private MemberJoinInfoMapper memberJoinInfoMapper;
    @Autowired
    private MemberMileMapper memberMileMapper;
    @Autowired
    private MemberWhiteAccountMapper memberWhiteAccountMapper;
    @Autowired
    private MemberLoginMapper memberLoginMapper;
    @Autowired
    private CashCeraMapper cashCeraMapper;
    @Autowired
    private CashCeraPointMapper cashCeraPointMapper;
    @Autowired
    private MemberAvatarCoinMapper memberAvatarCoinMapper;
    @Autowired
    private CharacInfoMapper characInfoMapper;

    @SneakyThrows
    public void login(String accountName, String password) {
        Assert.notEmpty(accountName, "账号名不能为空");
        Assert.notEmpty(password, "账号密码不能为空");
        boolean isWindows = System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
        Assert.isTrue(isWindows, "请确保游戏运行在Windows系统下");
        boolean isRunning = CommandUtils.exec("tasklist").contains("DNF.exe");
        Assert.isFalse(isRunning, "存在正在运行的游戏进程");

        int uid = accountMapper.findUidByAccountNameAndPassword(accountName, password);
        String token = SecurityUtils.getToken(uid);
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c DNF.exe " + token);
        refreshCharacters(accountName);
    }

    @SneakyThrows
    @Transactional
    public void register(String accountName, String password) {
        Assert.notEmpty(accountName, "账号名不能为空");
        Assert.notEmpty(password, "账号密码不能为空");

        Integer uid = accountMapper.findUidByAccountName(accountName);
        Assert.isNull(uid, "已存在相同名称的账号");
        Integer maxUid = accountMapper.findMaxUid();
        uid = maxUid == null ? 1 : maxUid + 1;

        Assert.equals(accountMapper.insert(uid, accountName, password), 1);
        Assert.equals(limitCreateCharacterMapper.insert(uid), 1);
        Assert.equals(memberInfoMapper.insert(uid), 1);
        Assert.equals(memberJoinInfoMapper.insert(uid), 1);
        Assert.equals(memberMileMapper.insert(uid), 1);
        Assert.equals(memberWhiteAccountMapper.insert(uid), 1);
        Assert.equals(memberLoginMapper.insert(uid), 1);
        Assert.equals(memberAvatarCoinMapper.insert(uid), 1);
        Assert.equals(cashCeraMapper.insert(uid), 1);
        Assert.equals(cashCeraPointMapper.insert(uid), 1);
        DialogUtils.showInfo("注册结果", "账号注册成功");
    }

    public void changePassword(String accountName, String oldPassword, String newPassword) {
        Assert.notEmpty(accountName, "账号名不能为空");
        Assert.notEmpty(oldPassword, "账号原密码不能为空");
        Assert.notEmpty(newPassword, "账号新密码不能为空");

        Integer uid = accountMapper.findUidByAccountNameAndPassword(accountName, oldPassword);
        Assert.notNull(uid, "账号不存在或密码错误");
        Assert.equals(accountMapper.setPassword(accountName, newPassword), 1);
        DialogUtils.showInfo("修改结果", "修改密码成功");
    }

    public void refreshCharacters(String accountName) {
        Assert.notEmpty(accountName, "账号名不能为空");

        Integer uid = accountMapper.findUidByAccountName(accountName);
        Assert.notNull(uid, "账号不存在");
        loadCharacters(uid);
        DialogUtils.showInfo("刷新完成", "请到管理员功能面板查看角色列表");

        AppContextUtils.putValue("uid", uid);
        AppContextUtils.putValue("accountName", accountName);
    }

    private void loadCharacters(int uid) {
        ManagementPanelController managementPanelController = AppContextUtils.getBean(ManagementPanelController.class);
        List<Charac> characters = characInfoMapper.getCharacters(uid);
        managementPanelController.setCharacters(characters);
    }

    public void setCera(Integer uid, String inputCera) {
        int cera = Integer.parseInt(inputCera);
        Assert.notNull(uid, "请确保已经登录游戏或刷新角色");
        Assert.isTrue(cera >= 0, "请确保输入的D币数值为非负整数");
        cashCeraMapper.update(uid, cera);
    }

    public void setCeraPoint(Integer uid, String inputCeraPoint) {
        int ceraPoint = Integer.parseInt(inputCeraPoint);
        Assert.notNull(uid, "请确保已经登录游戏或刷新角色");
        Assert.isTrue(ceraPoint >= 0, "请确保输入的D点数值为非负整数");
        cashCeraPointMapper.update(uid, ceraPoint);
    }

    public void setAvataCoin(Integer uid, String inputAvataCoin) {
        int avataCoin = Integer.parseInt(inputAvataCoin);
        Assert.notNull(uid, "请确保已经登录游戏或刷新角色");
        Assert.isTrue(avataCoin >= 0, "请确保输入的时装代币数量为非负整数");
        memberAvatarCoinMapper.update(uid, avataCoin);
    }
}
