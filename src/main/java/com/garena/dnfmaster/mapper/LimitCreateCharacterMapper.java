package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LimitCreateCharacterMapper {
    @Insert("insert into d_taiwan.limit_create_character (m_id) VALUES (#{uid})")
    int insert(int uid);

    @Update("update d_taiwan.limit_create_character set count=0 where true")
    int resetAllCount();
}
