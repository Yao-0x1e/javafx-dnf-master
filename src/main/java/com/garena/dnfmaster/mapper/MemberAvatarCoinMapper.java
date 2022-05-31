package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberAvatarCoinMapper {
    @Insert("insert into taiwan_cain_2nd.member_avatar_coin (m_id) VALUES (#{uid})")
    int insert(int uid);

    @Update("update taiwan_cain_2nd.member_avatar_coin set avatar_coin=#{coin} where m_id=#{uid}")
    int update(int uid, int coin);
}
