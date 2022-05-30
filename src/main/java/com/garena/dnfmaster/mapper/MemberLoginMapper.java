package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberLoginMapper {
    @Insert("insert into taiwan_login.member_login (m_id) VALUES (#{uid})")
    int insert(int uid);
}
