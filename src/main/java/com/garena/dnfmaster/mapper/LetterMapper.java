package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LetterMapper {
    @Delete("truncate taiwan_cain_2nd.letter")
    int deleteAll();

    @Insert("insert into taiwan_cain_2nd.letter (letter_id,charac_no,send_charac_name,letter_text,reg_date,stat) values (#{letterId},#{characNo},#{sender},#{letterText},NOW(),1)")
    int insert(int letterId, int characNo, String sender, String letterText);

    @Select("select max(letter_id) from taiwan_cain_2nd.letter")
    Integer findMaxLetterId();
}
