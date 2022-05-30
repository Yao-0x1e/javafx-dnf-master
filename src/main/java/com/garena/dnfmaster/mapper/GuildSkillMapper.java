package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GuildSkillMapper {
    @Update("update d_guild.guild_skill set skill_slot=0xC800000008C900000008,used_sp=2 where guild_id=#{guildId}")
    int setMaxSkill(int guildId);
}
