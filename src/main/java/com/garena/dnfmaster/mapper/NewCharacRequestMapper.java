package com.garena.dnfmaster.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NewCharacRequestMapper {
    @Update("update taiwan_cain.new_charac_quest " +
            "set play_1_trigger=0,play_2_trigger=0,play_3_trigger=0,play_4_trigger=0,play_5_trigger=0,play_6_trigger=0,play_7_trigger=0,play_8_trigger=0,play_9_trigger=0,play_10_trigger=0,play_11_trigger=0,play_12_trigger=0,play_13_trigger=0,play_14_trigger=0,play_15_trigger=0,play_16_trigger=0,play_17_trigger=0,play_18_trigger=0,play_19_trigger=0,play_20_trigger=0 " +
            "where charac_no=#{characNo}")
    int clearQuests(int characNo);

    @Update("update taiwan_cain.new_charac_quest " +
            "set clear_quest=#{clearQuest},quest_notify=#{questNotify},play_1=0,play_1_trigger=0,play_2=0,play_2_trigger=0,play_3=0,play_3_trigger=0,play_4=0,play_4_trigger=0,play_5=0,play_5_trigger=0,play_6=0,play_6_trigger=0,play_7=0,play_7_trigger=0,play_8=0,play_8_trigger=0,play_9=0,play_9_trigger=0,play_10=0,play_10_trigger=0,auto_clear=0,play_11=0,play_11_trigger=0,play_12=0,play_12_trigger=0,play_13=0,play_13_trigger=0,play_14=0,play_14_trigger=0,play_15=0,play_15_trigger=0,play_16=0,play_16_trigger=0,play_17=0,play_17_trigger=0,play_18=0,play_18_trigger=0,play_19=0,play_19_trigger=0,play_20=0,play_20_trigger=0,urgentQuestIndex=-1 " +
            "where charac_no=#{characNo}")
    int clearAllQuests(int characNo, byte[] clearQuest, byte[] questNotify);
}
