package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostalMapper {
    @Delete("truncate taiwan_cain_2nd.postal")
    int deleteAll();

    @Insert("insert into taiwan_cain_2nd.postal (occ_time,send_charac_name,receive_charac_no,amplify_option,seperate_upgrade,seal_flag,item_id,add_info,upgrade,gold,letter_id,avata_flag,creature_flag,random_option,item_guid) " +
            "values (NOW(),#{sender},#{characNo},#{amplifyOption},#{seperateUpgrade},#{sealFlag},#{itemId},#{itemQuantity},#{upgrade},#{gold},#{letterId},#{avataFlag},#{creatureFlag},'','')")
    int insert(String sender, int characNo, int amplifyOption, int seperateUpgrade, int sealFlag, int itemId, int itemQuantity, int upgrade, int gold, int letterId, int avataFlag, int creatureFlag);
}