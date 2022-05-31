package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CashCeraPointMapper {
    @Insert("insert into taiwan_billing.cash_cera_point (account,cera_point,reg_date,mod_date) values (#{uid},0,NOW(),NOW())")
    int insert(int uid);

    @Update("update taiwan_billing.cash_cera_point set cera_point=#{ceraPoint} where account=#{uid}")
    int update(int uid, int certPoint);
}
