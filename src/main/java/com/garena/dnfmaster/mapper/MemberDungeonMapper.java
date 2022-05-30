package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberDungeonMapper {
    @Update("update taiwan_cain.member_dungeon set dungeon=#{dungeon} where m_id=#{uid}")
    int update(int uid, int dungeon);
}
