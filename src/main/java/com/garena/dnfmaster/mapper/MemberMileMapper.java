package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMileMapper {
    @Insert("insert into d_taiwan.member_miles (m_id) VALUES (#{uid})")
    int insert(int uid);
}
