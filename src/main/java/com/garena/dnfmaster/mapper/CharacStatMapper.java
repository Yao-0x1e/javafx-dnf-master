package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CharacStatMapper {
    @Update("update taiwan_cain.charac_stat set expert_job_exp=2054 where charac_no=#{characNo}")
    int setMaxExpertJobLevel(int characNo);

    @Update("update taiwan_cain.charac_stat set add_slot_flag=3 where charac_no=#{characNo}")
    int addSlots(int characNo);
}
