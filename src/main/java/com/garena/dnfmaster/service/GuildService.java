package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.annotation.DatabaseRequired;
import com.garena.dnfmaster.mapper.GuildInfoMapper;
import com.garena.dnfmaster.mapper.GuildSkillMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuildService {
    @Autowired
    private GuildInfoMapper guildInfoMapper;
    @Autowired
    private GuildSkillMapper guildSkillMapper;

    @DatabaseRequired
    @SneakyThrows
    @Transactional
    public void setMaxGuildProperties(String guildName) {
        Assert.notEmpty(guildName, "公会名称不能为空");
        Integer guildId = guildInfoMapper.findGuildId(guildName);
        if (guildId == null) {
            throw new Exception("不存在相应名称的公会：" + guildName);
        }
        guildInfoMapper.setMaxLevel(guildId);
        guildSkillMapper.setMaxSkill(guildId);
    }
}
