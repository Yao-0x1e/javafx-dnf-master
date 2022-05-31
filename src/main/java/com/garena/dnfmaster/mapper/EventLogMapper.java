package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventLogMapper {
    @Insert("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,#{eventType},#{param1},#{param2},0,0,0,0)")
    int insert(int eventType, int param1, int param2);

    @Delete("truncate table d_taiwan.dnf_event_log")
    int deleteAll();

    @Select("select event_type from d_taiwan.dnf_event_log")
    List<Integer> findAllEventTypes();
}
