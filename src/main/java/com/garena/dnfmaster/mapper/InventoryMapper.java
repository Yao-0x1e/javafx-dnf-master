package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InventoryMapper {
    @Update("update taiwan_cain_2nd.inventory set inventory='' where charac_no=#{characNo}")
    int clearInven(int characNo);

    @Update("update taiwan_cain_2nd.inventory set creature_flag=#{creatureFlag} where charac_no=#{characNo}")
    int setCreatureFlag(int characNo, int creatureFlag);
}
