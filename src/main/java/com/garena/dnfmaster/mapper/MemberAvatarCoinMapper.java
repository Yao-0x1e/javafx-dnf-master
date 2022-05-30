package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberAvatarCoinMapper {
    @Insert("insert into taiwan_cain_2nd.member_avatar_coin (m_id) VALUES (#{uid})")
    int insert(int uid);
}
