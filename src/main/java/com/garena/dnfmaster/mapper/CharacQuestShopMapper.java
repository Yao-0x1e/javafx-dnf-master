package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CharacQuestShopMapper {
    @Update("update taiwan_cain.charac_quest_shop set qp=#{qp} where charac_no=#{characNo}")
    int setQP(int characNo, int qp);
}
