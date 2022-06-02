package com.garena.dnfmaster.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TriggerManager {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createCharacterCreationTrigger() {
        jdbcTemplate.batchUpdate("use d_taiwan;",
                "create trigger limit_create_character_trigger before update on d_taiwan.limit_create_character for each row set NEW.count=0;",
                "update d_taiwan.limit_create_character set count=0 where true;"
        );
    }

    public void deleteCharacterCreationTrigger() {
        jdbcTemplate.batchUpdate("use d_taiwan;",
                "drop trigger if exists limit_create_character_trigger;"
        );
    }
}
