package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SkillMapper {
    @Update("update taiwan_cain_2nd.skill set remain_sp=#{sp},remain_sp_2nd=#{sp} where charac_no=#{characNo}")
    int setSP(int characNo, int sp);

    @Update("update taiwan_cain_2nd.skill set remain_sfp_1st=#{tp},remain_sfp_2nd=#{tp} where charac_no=#{characNo}")
    int setTP(int characNo, int tp);
}
