package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberInfoMapper {
    @Insert("insert into d_taiwan.member_info (m_id,user_id) VALUES (#{uid},#{uid})")
    int insert(int uid);


}
