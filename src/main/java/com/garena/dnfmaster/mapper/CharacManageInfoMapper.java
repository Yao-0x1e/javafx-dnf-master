package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CharacManageInfoMapper {
    @Update("update taiwan_cain.charac_manage_info set max_equip_level=85 where charac_no=#{characNo}")
    int setMaxEquipLevel(int characNo);

    @Insert("insert into taiwan_cain.charac_manage_info(charac_no,tag_charac_no,striker_skill_index,max_equip_level) values (#{characNo},0,0,85)")
    int insertMaxEquipLevel(int characNo);
}
