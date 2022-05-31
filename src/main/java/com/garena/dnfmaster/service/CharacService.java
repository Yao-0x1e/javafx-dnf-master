package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.constant.PvpGrade;
import com.garena.dnfmaster.mapper.CharacQuestShopMapper;
import com.garena.dnfmaster.mapper.PvpResultMapper;
import com.garena.dnfmaster.mapper.SkillMapper;
import com.garena.dnfmaster.util.DialogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacService {
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private CharacQuestShopMapper characQuestShopMapper;
    @Autowired
    private PvpResultMapper pvpResultMapper;

    public void setSP(int characNo, String inputSP) {
        int sp = Integer.parseInt(inputSP);
        Assert.isTrue(sp >= 0, "请确保输入的SP数值为非负整数");

        skillMapper.setSP(characNo, sp);
        DialogUtils.showInfo("数值修改", "成功修改SP（重新登录游戏后即可生效）");
    }

    public void setTP(int characNo, String inputTP) {
        int tp = Integer.parseInt(inputTP);
        Assert.isTrue(tp >= 0, "请确保输入的TP数值为非负整数");

        skillMapper.setTP(characNo, tp);
        DialogUtils.showInfo("数值修改", "成功修改TP（重新登录游戏后即可生效）");
    }

    public void setQP(int characNo, String inputQP) {
        int qp = Integer.parseInt(inputQP);
        Assert.isTrue(qp >= 0, "请确保输入的QP数值为非负整数");

        characQuestShopMapper.setQP(characNo, qp);
        DialogUtils.showInfo("数值修改", "成功修改QP（重新登录游戏后即可生效）");
    }


    public void setPvpGrade(int characNo, String inputPvpGrade) {
        int pvpGrade = Integer.parseInt(inputPvpGrade);
        Assert.checkBetween(pvpGrade, PvpGrade.MIN_VALUE, PvpGrade.MAX_VALUE, "段位不在合法区间内");

        pvpResultMapper.setPvpGrade(characNo, pvpGrade);
        DialogUtils.showInfo("数值修改", "成功修改段位（重新登录游戏后即可生效）");
    }

    public void setPvpPoint(int characNo, String inputPvpPoint) {
        int pvpPoint = Integer.parseInt(inputPvpPoint);
        Assert.isTrue(pvpPoint >= 0, "请确保输入的胜点数值为非负整数");

        pvpResultMapper.setPvpPoint(characNo, pvpPoint);
        DialogUtils.showInfo("数值修改", "成功修改胜点（重新登录游戏后即可生效）");
    }

    public void setPvpWin(int characNo, String inputPvpWin) {
        int win = Integer.parseInt(inputPvpWin);
        Assert.isTrue(win >= 0, "请确保输入的胜场数值为非负整数");

        pvpResultMapper.setPvpWin(characNo, win);
        DialogUtils.showInfo("数值修改", "成功修改胜场（重新登录游戏后即可生效）");
    }

    public void setPvpLose(int characNo, String inputPvpLose) {
        int lose = Integer.parseInt(inputPvpLose);
        Assert.isTrue(lose >= 0, "请确保输入的败场数值为非负整数");

        pvpResultMapper.setPvpLose(characNo, lose);
        DialogUtils.showInfo("数值修改", "成功修改败场（重新登录游戏后即可生效）");
    }
}
