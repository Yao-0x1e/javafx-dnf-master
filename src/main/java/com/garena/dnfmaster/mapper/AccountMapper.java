package com.garena.dnfmaster.mapper;

import com.garena.dnfmaster.pojo.Charac;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * MyBatis查询数据不存在时的返回值为null或空列表
 */
@Mapper
public interface AccountMapper {
    @Select("select UID from d_taiwan.accounts where accountname=#{name} and password=md5(#{password})")
    Integer findUidByAccountNameAndPassword(String name, String password);

    @Select("select UID from d_taiwan.accounts where accountname=#{name}")
    Integer findUidByAccountName(String name);

    @Select("select max(UID) from d_taiwan.accounts")
    Integer findMaxUid();

    @Insert("insert into d_taiwan.accounts (uid,accountname,password) values (#{uid},#{name},md5(#{password}))")
    int insert(int uid, String name, String password);

    @Update("update d_taiwan.accounts set password=md5(#{password}) where accountname=#{name}")
    int setPassword(String name, String password);

    @Select("select charac_no as no,convert(unhex(hex(convert(charac_name using latin1))) using utf8) as name,lev as level from taiwan_cain.charac_info where m_id=#{uid} and delete_flag<>1")
    List<Charac> getCharacters(int uid);
}
