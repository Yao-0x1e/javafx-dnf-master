package com.garena.dnfmaster.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class MailDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void sendMail(int characNo, int itemId, int itemQuantity, int upgrade, int seperateUpgrade, int amplifyOption, int goldQuantity, boolean isAvata, boolean isCreature) {
        Integer letterId = this.jdbcTemplate.queryForObject("select max(letter_id) from taiwan_cain_2nd.postal", Integer.class);
        letterId = letterId == null ? 1 : letterId + 1;
        int avataFlag = isAvata ? 1 : 0;
        int creatureFlag = isCreature ? 1 : 0;
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.letter (letter_id,charac_no,send_charac_name,letter_text,reg_date,stat) values (?,?,'Garena','',NOW(),1)", letterId, characNo);
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.postal (occ_time,send_charac_name,receive_charac_no,amplify_option,amplify_value,seperate_upgrade,seal_flag,item_id,add_info,upgrade,gold,letter_id,avata_flag,creature_flag) values (NOW(),'Garena',?,?,0,?,0,?,?,?,?,?,?,?);", characNo, amplifyOption, seperateUpgrade, itemId, itemQuantity, upgrade, goldQuantity, letterId, avataFlag, creatureFlag);
    }

    public void clearMails() {
        this.jdbcTemplate.batchUpdate("truncate taiwan_cain_2nd.postal;", "truncate taiwan_cain_2nd.letter;");
    }
}
