package com.garena.dnfmaster.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class EventDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void multiplyDrops() {
        try {
            this.jdbcTemplate.queryForObject("select event_type from d_taiwan.dnf_event_log where event_type=7", Integer.class);
        } catch (Exception e) {
            this.jdbcTemplate.execute("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,7,2000,0,0,0,0,0)");
        }
    }

    public void unlimitFatiguePoint() {
        try {
            this.jdbcTemplate.queryForObject("select event_type from d_taiwan.dnf_event_log where event_type=1", Integer.class);
        } catch (Exception e) {
            this.jdbcTemplate.execute("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,1,1,0,0,0,0,0)");
        }
    }

    public void clearEvents() {
        this.jdbcTemplate.execute("truncate table d_taiwan.dnf_event_log");
    }

    public void unlimitCharacterCreation() {
        this.jdbcTemplate.batchUpdate(
                "use d_taiwan;",
                "drop trigger if exists limit_create_character_trigger;",
                "create trigger limit_create_character_trigger before update on d_taiwan.limit_create_character for each row set NEW.count=0;",
                "update d_taiwan.limit_create_character set count=0 where true;"
        );
    }

}
