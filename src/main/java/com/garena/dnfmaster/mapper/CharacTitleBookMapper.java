package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CharacTitleBookMapper {
    @Update("update taiwan_cain.charac_titlebook set specific_section=#{specificSection},general_section=#{generalSection},despair=#{despair} where charac_no=#{characNo}")
    int update(int characNo, byte[] specificSection, byte[] generalSection, byte[] despair);
}
