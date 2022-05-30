package com.garena.dnfmaster.repo;

import com.garena.dnfmaster.constant.PvpGrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class PvpRepo {
    @Autowired
    private JdbcTemplate jdbc;

    public int setPvpWin(int characNo, int win) {
        assert win >= 0;
        return jdbc.update("update taiwan_cain.pvp_result set win=? where charac_no=?", win, characNo);
    }

    public int setPvpLose(int characNo, int lose) {
        assert lose >= 0;
        return jdbc.update("update taiwan_cain.pvp_result set lose=? where charac_no=?", lose, characNo);
    }

    public int setPvpPoint(int characNo, int winPoint) {
        assert winPoint >= 0;
        return jdbc.update("update taiwan_cain.pvp_result set win_point=? where charac_no=?", winPoint, characNo);
    }

    public int setPvpGrade(int characNo, int pvpGrade) {
        assert pvpGrade >= PvpGrade.MIN_VALUE && pvpGrade <= PvpGrade.MAX_VALUE;
        return jdbc.update("update taiwan_cain.pvp_result set pvp_grade=? where charac_no=?", pvpGrade, characNo);
    }
}
