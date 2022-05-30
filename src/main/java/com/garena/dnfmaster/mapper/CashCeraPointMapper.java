package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CashCeraPointMapper {
    @Insert("insert into taiwan_billing.cash_cera_point (account,cera_point,reg_date,mod_date) values (#{uid},0,NOW(),NOW())")
    int insert(int uid);
}
