package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CreatureItemMapper {
    @Update("delete from taiwan_cain_2nd.creature_items where charac_no=#{characNo}")
    int clearCreatures(int characNo);

    @Select("select max(slot) from taiwan_cain_2nd.creature_items where charac_no=#{characNo} and slot<238")
    Integer findMaxCreatureSlot(int characNo);

    @Insert("insert into taiwan_cain_2nd.creature_items(charac_no,slot,it_id,reg_date,stomach,creature_type) values (#{characNo},#{slot},#{itemId},NOW(),100,#{creatureType})")
    int addCreature(int characNo, int slot, int itemId, int creatureType);
}
