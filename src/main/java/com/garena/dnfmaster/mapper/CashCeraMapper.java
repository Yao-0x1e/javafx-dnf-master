package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CashCeraMapper {
    @Insert("insert into taiwan_billing.cash_cera (account,cera,mod_tran,mod_date,reg_date) VALUES (#{uid},0,0,NOW(),NOW())")
    int insert(int uid);

    @Update("update taiwan_billing.cash_cera set cera=#{cera} where account=#{uid}")
    int update(int uid, int cera);
}
