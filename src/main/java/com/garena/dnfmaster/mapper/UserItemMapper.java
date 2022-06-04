package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserItemMapper {
    @Delete("delete from taiwan_cain_2nd.user_items where charac_no=#{characNo}")
    int clearAvatas(int characNo);

    @Select("select max(slot) from taiwan_cain_2nd.user_items where charac_no=#{characNo} and slot>=10")
    Integer findMaxAvatarSlot(int characNo);

    @Insert("insert into taiwan_cain_2nd.user_items(charac_no,slot,it_id,expire_date,obtain_from,reg_date,ipg_agency_no,ability_no,jewel_socket) values (#{characNo},#{slot},#{itemId},'9999-12-31 23:59:59',0,NOW(),#{ipgAgencyNo},#{abilityNo},0x010000000000010000000000000000000000000000000000000000000000)")
    int addAvata(int characNo, int slot, int itemId, int abilityNo, String ipgAgencyNo);

    @Select("select count(ui_id) from taiwan_cain_2nd.user_items where charac_no=#{characNo} and slot>=0 and slot<10")
    Integer countEquipAvatas(int characNo);
}
