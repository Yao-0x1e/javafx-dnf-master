package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GuildInfoMapper {
    @Select("select guild_id from d_guild.guild_info where convert(unhex(hex(convert(guild_name using latin1))) using utf8)=#{name}")
    Integer findGuildId(String name);

    @Update("update d_guild.guild_info set lev=16,ability=2,guild_exp=2900000 where guild_id=#{guildId}")
    int setMaxLevel(int guildId);
}
