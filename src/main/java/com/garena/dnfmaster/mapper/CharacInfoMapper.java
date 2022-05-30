package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CharacInfoMapper {
    @Select("select job from taiwan_cain.charac_info where charac_no=#{characNo}")
    Integer findJob(int characNo);

    @Select("select expert_job from taiwan_cain.charac_info where charac_no=#{characNo}")
    Integer findExpertJob(int characNo);

    @Update("update taiwan_cain.charac_info set expert_job=#{expertJob} where charac_no=#{characNo}")
    int setExpertJob(int characNo, int expertJob);

    @Update("update taiwan_cain.charac_info set grow_type=#{growType} where charac_no=#{characNo}")
    int setGrowType(int characNo, int growType);

    @Update("update taiwan_cain.charac_info set inven_weight=10000000 where charac_no=#{characNo}")
    int setMaxInvenWeight(int characNo);
}
