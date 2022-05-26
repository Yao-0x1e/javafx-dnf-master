package com.garena.dnfmaster.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class PvpDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setPvpWin(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set win=? where charac_no=?", val, characNo);
    }

    public void setPvpLose(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set lose=? where charac_no=?", val, characNo);
    }

    public void setPvpPoint(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set win_point=? where charac_no=?", val, characNo);
    }

    public void setPvpGrade(int characNo, int val) throws Exception {
        assert val >= 0 && val <= 34;
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set pvp_grade=? where charac_no=?", val, characNo);
    }
}
