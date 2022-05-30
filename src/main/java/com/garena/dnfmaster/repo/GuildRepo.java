package com.garena.dnfmaster.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class GuildRepo {
    @Autowired
    private JdbcTemplate jdbc;

    public Integer findGuildId(String guildName) {
        assert guildName != null;
        return jdbc.queryForObject("select guild_id from d_guild.guild_info where convert(unhex(hex(convert(guild_name using latin1))) using utf8)=?", Integer.class, guildName);
    }

    public int setMaxGuildLevel(int guildId) {
        assert guildId >= 0;
        return jdbc.update("update d_guild.guild_info set lev=16,ability=2,guild_exp=2900000 where guild_id=?; update d_guild.guild_skill set skill_slot=0xC800000008C900000008,used_sp=2 where guild_id=?;", guildId, guildId);
    }
}
