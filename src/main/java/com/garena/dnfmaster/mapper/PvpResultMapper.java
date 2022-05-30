package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PvpResultMapper {
    @Update("update taiwan_cain.pvp_result set win=#{win} where charac_no=#{characNo}")
    int setPvpWin(int characNo, int win);

    @Update("update taiwan_cain.pvp_result set lose=#{lose} where charac_no=#{characNo}")
    int setPvpLose(int characNo, int lose);

    @Update("update taiwan_cain.pvp_result set win_point=#{winPoint} where charac_no=#{characNo}")
    int setPvpPoint(int characNo, int winPoint);

    @Update("update taiwan_cain.pvp_result set pvp_grade=#{pvpGrade} where charac_no=#{characNo}")
    int setPvpGrade(int characNo, int pvpGrade);
}
