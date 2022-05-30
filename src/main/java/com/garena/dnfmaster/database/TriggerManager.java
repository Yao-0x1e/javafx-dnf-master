package com.garena.dnfmaster.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TriggerManager {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createCharacterCreationTrigger() {
        jdbcTemplate.execute("use d_taiwan; create trigger limit_create_character_trigger before update on d_taiwan.limit_create_character for each row set NEW.count=0;");
    }

    public void deleteCharacterCreationTrigger() {
        jdbcTemplate.execute("use d_taiwan; drop trigger if exists limit_create_character_trigger;");
    }
}
