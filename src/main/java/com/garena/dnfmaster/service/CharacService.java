package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.constant.ExpertJob;
import com.garena.dnfmaster.constant.GrowType;
import com.garena.dnfmaster.constant.PvpGrade;
import com.garena.dnfmaster.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacService {
    @Autowired
    private SkillMapper skillMapper;
    @Autowired
    private CharacQuestShopMapper characQuestShopMapper;
    @Autowired
    private PvpResultMapper pvpResultMapper;
    @Autowired
    private CharacInfoMapper characInfoMapper;
    @Autowired
    private GuildMemberMapper guildMemberMapper;

    public void setSP(int characNo, String inputSP) {
        int sp = Integer.parseInt(inputSP);
        Assert.isTrue(sp >= 0, "请确保输入的SP数值为非负整数");
        skillMapper.setSP(characNo, sp);
    }

    public void setTP(int characNo, String inputTP) {
        int tp = Integer.parseInt(inputTP);
        Assert.isTrue(tp >= 0, "请确保输入的TP数值为非负整数");
        skillMapper.setTP(characNo, tp);
    }

    public void setQP(int characNo, String inputQP) {
        int qp = Integer.parseInt(inputQP);
        Assert.isTrue(qp >= 0, "请确保输入的QP数值为非负整数");
        characQuestShopMapper.setQP(characNo, qp);
    }


    public void setPvpGrade(int characNo, String inputPvpGrade) {
        int pvpGrade = Integer.parseInt(inputPvpGrade);
        Assert.checkBetween(pvpGrade, PvpGrade.MIN_VALUE, PvpGrade.MAX_VALUE, "段位不在合法区间内");
        pvpResultMapper.setPvpGrade(characNo, pvpGrade);
    }

    public void setPvpPoint(int characNo, String inputPvpPoint) {
        int pvpPoint = Integer.parseInt(inputPvpPoint);
        Assert.isTrue(pvpPoint >= 0, "请确保输入的胜点数值为非负整数");
        pvpResultMapper.setPvpPoint(characNo, pvpPoint);
    }

    public void setPvpWin(int characNo, String inputPvpWin) {
        int win = Integer.parseInt(inputPvpWin);
        Assert.isTrue(win >= 0, "请确保输入的胜场数值为非负整数");
        pvpResultMapper.setPvpWin(characNo, win);
    }

    public void setPvpLose(int characNo, String inputPvpLose) {
        int lose = Integer.parseInt(inputPvpLose);
        Assert.isTrue(lose >= 0, "请确保输入的败场数值为非负整数");
        pvpResultMapper.setPvpLose(characNo, lose);
    }

    @Transactional
    public void setGrowType(int characNo, int growType) {
        Assert.isTrue((growType >= GrowType.MIN_JOB_VALUE && growType <= GrowType.MAX_JOB_VALUE) || (growType >= GrowType.MIN_AWAKE_VALUE && growType <= GrowType.MAX_AWAKE_VALUE), "主职业的数值不在合法区间内");
        int job = characInfoMapper.findJob(characNo);
        Assert.isFalse(job == 9 || job == 10, "黑暗骑士和缔造者不支持转职或觉醒");
        Assert.isFalse((job == 6 || job == 8) && (growType == 3 || growType == 4 || growType == 19 || growType == 20), "男法师和盗贼只有两种职业或觉醒");

        characInfoMapper.setGrowType(characNo, growType);
        guildMemberMapper.setGrowType(characNo, growType);
    }

    public void setExpertJob(int characNo, int expertJob) {
        Assert.isTrue(expertJob >= ExpertJob.MIN_VALUE && expertJob <= ExpertJob.MAX_VALUE, "副职业的数值不在合法区间内");
        characInfoMapper.setExpertJob(characNo, expertJob);
    }

}
