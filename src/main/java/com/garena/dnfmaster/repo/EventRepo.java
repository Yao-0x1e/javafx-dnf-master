package com.garena.dnfmaster.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class EventRepo {
    @Autowired
    private JdbcTemplate jdbc;

    public List<Integer> findAllEventTypes(int eventType) {
        List<Map<String, Object>> result = jdbc.queryForList("select event_type from d_taiwan.dnf_event_log");
        List<Integer> eventTypes = new ArrayList<>(result.size());
        result.forEach(map -> eventTypes.add((Integer) map.get("event_type")));
        return eventTypes;
    }

    public int enableMultiplyDrops() {
        return jdbc.update("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,7,2000,0,0,0,0,0)");
    }

    public int unlimitFatiguePoint() {
        return jdbc.update("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,1,1,0,0,0,0,0)");
    }

    public int unlimitCharacterCreation() {
        return jdbc.update("use d_taiwan;" +
                "drop trigger if exists limit_create_character_trigger;" +
                "create trigger limit_create_character_trigger before update on d_taiwan.limit_create_character for each row set NEW.count=0;" +
                "update d_taiwan.limit_create_character set count=0 where true;"
        );
    }

    public int clearEvents() {
        return jdbc.update("truncate table d_taiwan.dnf_event_log");
    }
}
