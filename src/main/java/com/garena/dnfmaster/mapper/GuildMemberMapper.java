package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GuildMemberMapper {
    @Update("update d_guild.guild_member set grow_type=#{growType} where charac_no=#{characNo}")
    int setGrowType(int characNo, int growType);
}
