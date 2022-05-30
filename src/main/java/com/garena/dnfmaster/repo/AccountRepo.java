package com.garena.dnfmaster.repo;

import com.garena.dnfmaster.pojo.Charac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class AccountRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Note: JdbcTemplate.queryForObject throws exception only if the size of returned rows equals 0
     */
    public Integer findUid(String accountName, String password) {
        return jdbcTemplate.queryForObject("select UID from d_taiwan.accounts where accountname=? and password=md5(?)", Integer.class, accountName, password);
    }

    public Integer findUid(String accountName) {
        return jdbcTemplate.queryForObject("select UID from d_taiwan.accounts where accountname=?", Integer.class, accountName);
    }

    public int addAccount(String accountName, String password) {
        Integer uid = jdbcTemplate.queryForObject("select ifnull((select max(UID) from d_taiwan.accounts), 0)+1", Integer.class);
        int rows = 0;
        rows += jdbcTemplate.update("insert into d_taiwan.accounts (uid,accountname,password) VALUES (?,?,md5(?))", uid, accountName, password);
        rows += jdbcTemplate.update("insert into d_taiwan.limit_create_character (m_id) VALUES (?)", uid);
        rows += jdbcTemplate.update("insert into d_taiwan.member_info (m_id,user_id) VALUES (?,?)", uid, uid);
        rows += jdbcTemplate.update("insert into d_taiwan.member_join_info (m_id) VALUES (?)", uid);
        rows += jdbcTemplate.update("insert into d_taiwan.member_miles (m_id) VALUES (?)", uid);
        rows += jdbcTemplate.update("insert into d_taiwan.member_white_account (m_id) VALUES (?)", uid);
        rows += jdbcTemplate.update("insert into taiwan_login.member_login (m_id) VALUES (?)", uid);
        rows += jdbcTemplate.update("insert into taiwan_billing.cash_cera (account,cera,mod_tran,mod_date,reg_date) VALUES (?,0,0,NOW(),NOW())", uid);
        rows += jdbcTemplate.update("insert into taiwan_billing.cash_cera_point (account,cera_point,reg_date,mod_date) VALUES (?,0,NOW(),NOW())", uid);
        rows += jdbcTemplate.update("insert into taiwan_cain_2nd.member_avatar_coin (m_id) VALUES (?)", uid);
        return rows;
    }

    public int setPassword(String accountName, String password) {
        return jdbcTemplate.update("update d_taiwan.accounts set password=md5(?) where accountname=?", password, accountName);
    }

    public List<Charac> findCharacters(int uid) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select charac_no,convert(unhex(hex(convert(charac_name using latin1))) using utf8) as name,lev from taiwan_cain.charac_info where m_id=? and delete_flag<>1;", uid);
        List<Charac> characters = new ArrayList<>(list.size());
        list.forEach((map) -> {
            int id = (Integer) map.get("charac_no");
            String name = (String) map.get("name");
            int level = (Integer) map.get("lev");
            characters.add(new Charac(id, name, level));
        });
        return characters;
    }

    public int setCashCera(int uid, int val) {
        return jdbcTemplate.update("update taiwan_billing.cash_cera set cera=? where account=?", val, uid);
    }

    public int setDungeonsUltimate(int uid) {
        String dungeon = "2|3,3|3,4|3,5|3,6|3,7|3,8|3,9|3,11|3,12|3,13|3,14|3,15|3,17|3,21|3,22|3,23|3,24|3,25|3,26|3,27|3,31|3,32|3,33|3,34|3,35|3,36|3,37|3,40|3,42|3,43|3,44|3,45|3,50|3,51|3,52|3,53|3,60|3,61|3,65|2,66|1,67|2,70|3,71|3,72|3,73|3,74|3,75|3,76|3,77|3,80|3,81|3,82|3,83|3,84|3,85|3,86|3,87|3,88|3,89|3,90|3,91|2,92|3,93|3,100|3,101|3,102|3,103|3,104|3,110|3,111|3,112|3,140|3,141|3,502|3,511|3,521|3,1000|3,1500|3,1501|3,1502|3,1504|1,1506|3,3506|3,10000|3";
        return jdbcTemplate.update("update taiwan_cain.member_dungeon set dungeon=? where m_id=?", dungeon, uid);
    }
}
