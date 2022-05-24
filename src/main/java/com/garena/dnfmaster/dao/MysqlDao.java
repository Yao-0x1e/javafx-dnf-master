package com.garena.dnfmaster.dao;

import com.garena.dnfmaster.pojo.Charac;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SqlNoDataSourceInspection")
public class MysqlDao {
    private final BasicDataSource dataSource = new BasicDataSource();
    private final JdbcTemplate jdbcTemplate;

    public MysqlDao(String host, int port, String username, String password) {
        String url = String.format("dao:mysql://%s:%d?characterEncoding=UTF-8&sslMode=PREFERRED", host, port);
        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setUrl(url);
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
        this.dataSource.setMaxConnLifetimeMillis(1800000L);
        this.dataSource.setMaxWaitMillis(1000L);
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public void close() throws SQLException {
        this.dataSource.close();
    }

    public void setPvpWin(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set win=? where charac_no=?", val, characNo);
    }

    public void setPvpLose(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set lose=? where charac_no=?", val, characNo);
    }

    public void setPvpPoint(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set win_point=? where charac_no=?", val, characNo);
    }

    public void setPvpGrade(int characNo, int val) throws Exception {
        assert val >= 0 && val <= 34;
        this.jdbcTemplate.update("update taiwan_cain.pvp_result set pvp_grade=? where charac_no=?", val, characNo);
    }

    public void setSP(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain_2nd.skill set remain_sp=?,remain_sp_2nd=? where charac_no=?", val, val, characNo);
    }

    public void setTP(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain_2nd.skill set remain_sfp_1st=?,remain_sfp_2nd=? where charac_no=?", val, val, characNo);
    }

    public void setQP(int characNo, int val) throws Exception {
        this.jdbcTemplate.update("update taiwan_cain.charac_quest_shop set qp=? where charac_no=?", val, characNo);
    }

    public void setMaxEquipLevel(int characNo) throws Exception {
        int maxEquipLevel = 85;
        int res = this.jdbcTemplate.update("update taiwan_cain.charac_manage_info set max_equip_level=? where charac_no=?", maxEquipLevel, characNo);
        if (res == 0) {
            this.jdbcTemplate.update("insert into taiwan_cain.charac_manage_info(charac_no, tag_charac_no, striker_skill_index, max_equip_level) values(?,0,0,?)", characNo, maxEquipLevel);
        }
    }

    public void setMaxInvenWeight(int characNo) throws Exception {
        int maxInventoryWeight = 10000000;
        this.jdbcTemplate.update("update taiwan_cain.charac_info set inven_weight=? where charac_no=?", maxInventoryWeight, characNo);
    }

    public void setMaxGuildLevel(String guildName) throws Exception {
        Integer guildId = this.jdbcTemplate.queryForObject("select guild_id from d_guild.guild_info where convert(unhex(hex(convert(guild_name using latin1))) using utf8)=?", new Object[]{guildName}, Integer.class);
        assert guildId != null;
        this.jdbcTemplate.update("update d_guild.guild_info set lev=16,ability=2,guild_exp=2900000 where guild_id=?", guildId);
        this.jdbcTemplate.update("update d_guild.guild_skill set skill_slot=0xC800000008C900000008,used_sp=2 where guild_id=?", guildId);
    }

    public void setGrowType(int characNo, int val) throws Exception {
        assert val >= 0 && val <= 8;
        if (val >= 5) {
            val += 12;
        }

        Integer job = this.jdbcTemplate.queryForObject("select job from taiwan_cain.charac_info where charac_no=?", new Object[]{characNo}, Integer.class);
        assert job != null;
        assert !(job == 6 || job == 8) || !(val == 3 || val == 4 || val == 19 || val == 20);

        jdbcTemplate.update("update taiwan_cain.charac_info set grow_type=? where charac_no=?", val, characNo);
        jdbcTemplate.update("update d_guild.guild_member set grow_type=? where charac_no=?", val, characNo);
    }

    public void setMaxExpertJobLevel(int characNo) {
        Integer expertJob = this.jdbcTemplate.queryForObject("select expert_job from taiwan_cain.charac_info where charac_no=? and expert_job!=0", new Object[]{characNo}, Integer.class);
        assert expertJob != null;
        this.jdbcTemplate.update("update taiwan_cain.charac_stat set expert_job_exp=2054 where charac_no=?", characNo);
    }

    public void setExpertJob(int characNo, int val) {
        assert val >= 0 && val <= 4;
        this.jdbcTemplate.update("update taiwan_cain.charac_info set expert_job=? where charac_no=?", val, characNo);
    }

    public void setDungeonsUltimate(int uid) {
        String dungeon = "2|3,3|3,4|3,5|3,6|3,7|3,8|3,9|3,11|3,12|3,13|3,14|3,15|3,17|3,21|3,22|3,23|3,24|3,25|3,26|3,27|3,31|3,32|3,33|3,34|3,35|3,36|3,37|3,40|3,42|3,43|3,44|3,45|3,50|3,51|3,52|3,53|3,60|3,61|3,65|2,66|1,67|2,70|3,71|3,72|3,73|3,74|3,75|3,76|3,77|3,80|3,81|3,82|3,83|3,84|3,85|3,86|3,87|3,88|3,89|3,90|3,91|2,92|3,93|3,100|3,101|3,102|3,103|3,104|3,110|3,111|3,112|3,140|3,141|3,502|3,511|3,521|3,1000|3,1500|3,1501|3,1502|3,1504|1,1506|3,3506|3,10000|3";
        this.jdbcTemplate.update("update taiwan_cain.member_dungeon set dungeon=? where m_id=?", dungeon, uid);
    }

    public void setCashCera(int uid, int val) {
        this.jdbcTemplate.update("update taiwan_billing.cash_cera set cera=? where account=?", val, uid);
    }

    public void addSlots(int characNo) {
        this.jdbcTemplate.update("update taiwan_cain.charac_stat set add_slot_flag=3 where charac_no=?", characNo);
    }

    public void unlimitCharacterCreation() {
        this.jdbcTemplate.batchUpdate(
                "use d_taiwan;",
                "drop trigger if exists limit_create_character_trigger;",
                "create trigger limit_create_character_trigger before update on d_taiwan.limit_create_character for each row set NEW.count=0;",
                "update d_taiwan.limit_create_character set count=0 where true;"
        );
    }

    public void multiplyDrops() {
        try {
            this.jdbcTemplate.queryForObject("select event_type from d_taiwan.dnf_event_log where event_type=7", Integer.class);
        } catch (Exception e) {
            this.jdbcTemplate.execute("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,7,2000,0,0,0,0,0)");
        }
    }

    public void unlimitFatiguePoint() {
        try {
            this.jdbcTemplate.queryForObject("select event_type from d_taiwan.dnf_event_log where event_type=1", Integer.class);
        } catch (Exception e) {
            this.jdbcTemplate.execute("insert into d_taiwan.dnf_event_log (occ_time,event_type,parameter1,parameter2,server_id,event_flag,start_time,end_time) values (0,1,1,0,0,0,0,0)");
        }
    }

    public void sendMail(int characNo, int itemId, int itemQuantity, int upgrade, int seperateUpgrade, int amplifyOption, int goldQuantity, boolean isAvata, boolean isCreature) {
        Integer letterId = this.jdbcTemplate.queryForObject("select max(letter_id) from taiwan_cain_2nd.postal", Integer.class);
        letterId = letterId == null ? 1 : letterId + 1;
        int avataFlag = isAvata ? 1 : 0;
        int creatureFlag = isCreature ? 1 : 0;
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.letter (letter_id,charac_no,send_charac_name,letter_text,reg_date,stat) values (?,?,'Garena','',NOW(),1)", letterId, characNo);
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.postal (occ_time,send_charac_name,receive_charac_no,amplify_option,amplify_value,seperate_upgrade,seal_flag,item_id,add_info,upgrade,gold,letter_id,avata_flag,creature_flag) values (NOW(),'Garena',?,?,0,?,0,?,?,?,?,?,?,?);", characNo, amplifyOption, seperateUpgrade, itemId, itemQuantity, upgrade, goldQuantity, letterId, avataFlag, creatureFlag);
    }

    public void clearMails() {
        this.jdbcTemplate.batchUpdate("truncate taiwan_cain_2nd.postal;", "truncate taiwan_cain_2nd.letter;");
    }

    public void clearInven(int characNo) {
        this.jdbcTemplate.update("update taiwan_cain_2nd.inventory set inventory='' where charac_no=?", characNo);
    }

    public void clearEvents() {
        this.jdbcTemplate.execute("truncate table d_taiwan.dnf_event_log");
    }

    public void clearCreatures(int characNo) {
        this.jdbcTemplate.update("delete from taiwan_cain_2nd.creature_items where charac_no=?", characNo);
    }

    public void clearAvatas(int characNo) {
        this.jdbcTemplate.update("delete from taiwan_cain_2nd.user_items where charac_no=?", characNo);
    }

    public void addCreature(int characNo, int itemId, int creatureType) {
        assert creatureType == 0 || creatureType == 1;

        Integer slot = this.jdbcTemplate.queryForObject("select max(slot) from taiwan_cain_2nd.creature_items where charac_no=? and slot<238", new Object[]{characNo}, Integer.class);
        slot = slot == null ? 0 : slot + 1;
        this.jdbcTemplate.update("update taiwan_cain_2nd.inventory set creature_flag=1 where charac_no=?", characNo);
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.creature_items(charac_no,slot,it_id,reg_date,stomach,creature_type) values (?,?,?,NOW(),100,?);", characNo, slot, itemId, creatureType);
    }

    public void emptyQuests(int characNo, boolean all) {
        if (!all) {
            this.jdbcTemplate.update("update taiwan_cain.new_charac_quest set play_1_trigger=0,play_2_trigger=0,play_3_trigger=0,play_4_trigger=0,play_5_trigger=0,play_6_trigger=0,play_7_trigger=0,play_8_trigger=0,play_9_trigger=0,play_10_trigger=0,play_11_trigger=0,play_12_trigger=0,play_13_trigger=0,play_14_trigger=0,play_15_trigger=0,play_16_trigger=0,play_17_trigger=0,play_18_trigger=0,play_19_trigger=0,play_20_trigger=0 where charac_no=?", characNo);
        } else {
            Integer job = this.jdbcTemplate.queryForObject("select job from taiwan_cain.charac_info where charac_no=?", new Object[]{characNo}, Integer.class);
            assert job != null;

            String[] clearQuests = new String[]{
                    "0x30750000789CEDD8DB72C220100050F8FF9FEE8C6D080BA492943A193DE7C10B8605172460CA4FA5E797944B83EFAA8F174DBC14DFE6BACE16A92ACDE1696C8F72D09BABF24CEBE3FEE4D89F52D25F1AF21CCB5395B4AD17B9CBE9D9BE9DAA548D5A18AD4519FE69E3B7C6993297AB9CE2D05D4CEF41B5970FD6B8C1BEF4E0D7B7AAC1A566A77C1E5D7BA2EEB0E1C92934158FCA234356B37BDB762C8FD7479FDF4CD95D6CBB84951BA0D24879CA4DD172AF5861F7873B8E28B4FE6BA2F66BC51BFC209A33554EE5F4B22E8DF10096FB9DF5D226264EE26D855C8294437713B969A40B70D5A23021647F9EAE3EDBA671D78F7D7EEF27F95C1ED351CE8603D005CF718A0DEB9C3CC13E4FC376D54C8EAB716E4B463D879AF9F171AA656B41946602CD940EE7DB60FDDB3E296FEB45AFFE2F2C866EB73A717D1C7E9774F1D4D77CA370FBBA100EE04359386FA0B90FFF65388C2400EFCA3D0E000000000000000000000000000000000000002EF802C33903C7",
                    "0x30750000789CEDD8D192C2200C4051F2FF3FBD33B5A409499532D1E9EA3D2F6E91062494C23679A9BDAEA2559DC7ADDB1F43BCE62FC5DED3239952711FB923CA496F56C94CEB797FC4F7474B625537CEBEBC9941EBBD9030A657FB262E3313F57B3625E6A5C46927EA327963453F722E8C349FBAC5964F6EFB78B2F20663E9C9D357D560A9D9D92059DD0BF7A60D4F4EA1A978301E4B2E03756BFBE6634B523ACD6F983EDD5DF45D42E506481BD10F198ACA7D628835C1BFB0BFC01778D7448D6BC5173C10C3996A5FD3C379B3A00D3D4BC59D7569131327F1F106D1207AE81E220F8D8400AB8AC2B890F13C6DBEEBD338F4E398DF5A71DFE54ADF3DC758270908C17D8BF93D170F774FC7404C4AA7C6D8E4792C710F0790607EFC1CB36C154479BE32E695B38693F5AF7FA39776D1B3FF0BF3A1C7AD8E5F1FD3DFD2164F7DC32F72AFAF857000F0A358382F38DE76B54366DEA3DB652F5E0975B11C0080FF82771900000000000000000000000000000000000000000000000000000000000BFE00522603E7",
                    "0x30750000789CEDD8D972C220140050F8FF9FEE8C3584552183B68EE73CD44AE05E044296109F0ACFABA4AA85DFA6B77FAA78A1FC1AF33647A4AC34161F7D6794416FAE8A33D9FBFD89657F52495BB518E7B23C648376F4223663BADAB7A6D1A340D9AC15B3B56984EF39860776CDE4C759FEE9730D6228A7EEE2F00E9ABD7DB2066BB653B26B47D813E6618AC91CB15777A16D37F1E4129A8A47E6B602E70698BF92DF937497F93F9CBD747771DC25ECBC014A49D247AC8AB67BCB101F33FCBDF7177C94572DD4CEC3C04BF2BC55F54C753FCF9BE7CD0D398EE18BED9DF5D614134FE275839882A487EE2A7295A40970D5A63045C8F6793A3B762CE3A61FF9817338CEBF8331EB4E4013BCC898A6BE57EBDA8C0F7E6AA8323FAA9D5AD525C5C9011DD6C7D7C9B6AD0D51AA053453DA5D6F9DFDEF3892BEE69B5E28F7FBACF9F0BDE770A5775FB22C0E42EAA9530A60958D73C179B5DB3B64E535367B13B89EE4490B130DC0C7721103000000000000000000000000000000000000800B7E00149903E9",
                    "0x30750000789CEDD9DB72C2201400C09CFFFFE9CE54416E51C2D08EB5BB7D8812385040027AC44BC7EB2C396BE556F4FB4513EFA8DF465926452A52A3BA8C3DA29CB46655CCD43E6E4FD4EDC9297DD6AA9FEBF4A3E8B4D48AE8FAF46ADB9E14EAEF16A3568DD6A61EBED7F1A4391BEBF900E73D32D75771D443B7D8BD676D588BB66E5C619F7AF2E9DB55E156B3533E46792F941D563C3985A6E25188340975D5FB8A78ACA3C369FE86839777176997B07303942BC9976892B6FB9D2EBE0FB1FD057FC24F4DD47EADF8800F4473A68A239F5EF675637D008B7E67BDB58A8993785B2072907CE86E2237957401566D0A5385ECCFD3C5BD348DBB763CE677FA56218ABFDBA58B7532005DF068A6D8A8CCC513ECEB6E48B966FAB818E736A56A390C981FFF4EB16C6D88D24CA099D4E17C1BAC7FE94E7E5B2E7AE5776175E876AB53AF8FC3FFE5583CF5D5D5D60FDB857000FF9485F30D948FE1F6A782ABBF426ECD0600EFC3C30B00000000000000000000000000000000000000167C01520903DE",
                    "0x30750000789CEDD9DB8EC220100050F8FF9FDEC406E4564582A6BAE7BCA8588611908286F854787E49BEB47254BD3D69E285FA652CEBA4484569AC1EC6EE514EB25915675A1FE713EB7C72497F69D5CF7579283A2D6511BB3E7D35B7A94A65626934633F2E5B9CA6B36F247FDF5C5FC5500FDD62F79E54FBF8608D1BEC4B4FBE7DBB1ADC6A76CAC7D1B52FD41D363C3985A6E251B88F96BEBAACB463B93D1F2D23171CBBBCBB48BB849D1BA0DC487E884DD1761FEAE2638CED2FF80AEF9AA8FD5AF1035F88E64C15433EBDECEBC6FA0016FB9DF5D626264EE26D859883E4437713B969A40BB06A53982A647F9E2EDE4BD3B8CBE33EBFF36547E13160DD51BAFB11E34146758B619CE18B7798C79D500CE9541F17E3DC96D49943EF0BE6C7D5F3FB36C5B2B5214A3381664A87E33958FFD23BF965B9E895F3A20EDD6E75EAF571F859C2E2A9AFF944D5CD61211CC03F65E1BC80E68636FA97653AD46A0A8BF500E053DCAB0000000000000000000000000000000000000060C11FF3EE03DB",
                    "0x30750000789CEDD7DBB283200C4051F2FF3F7D665A8924040B0CB5B467AF875E5002124449F2527A7D8A9E6A3CAB3E7EB878C9FE95B24E8E54948AF98A9D511ABD99253DADC7FD11DB1F2DA94F35E36CCB533168B917528DE968DF862A155933D91A6BF6BAD58B03AB32F9FBFAC64A924DDDE4F036AADD9EACB8C1BAB471F7AD6A70A9DE292FD1B90375C3863BA750573C14E4F8987BA2E0168FA7F4B18E86D37CC3B4E9DB457E4B58F902A48DE897B8A2E56E1962BD1B77CC28E0BD6BA2D66BC50FDC106E4F75DCE7D57E73411B79F8A47EB35EDA44C74EDC57100DA29B6E17D935520598B5288C0959EFA78B63791A57FD38E7F7B99317FD4CAD310B135005173BC5F24325386B2EE38D4B4DAEE5ABB3B5962F713DC747ED9987EF9B1F5FD6DDFD14CBD682286E02F59486090CD6BF7C44FF968B5E39116C68FFAA63D7C7F05AD2E4AECF5D91797C4D8403807F8A857303F6195B6C76C7F3B220934C0600C0967840010000000000000000000000000000000000000000000000000000000030E10FE4BE03DB",
                    "0x30750000789CEDD8DB9283200C80E1E4FD5F7A67BA4B20075B64B0EBB4FF77612D4240C0038ABE24AFB35856E7B7E86327C413FF57C7322DD290AAEEA7D6A31CB46695CED45EB7477D7B2C256775FDECD365E8B4D60A4D7D7AB66DA70A0DA3E6466BBD87F3CC396CCEBE91FC7C737DA5E2876EB17B0F8ABD7DB0EA0A73EAC1D5B7ABC2AD66A7BC56794F942D2B9E9C4253F13050DBFCFDA7CBEEA7BDB13CF66D138EDF8CBD5DB4B7849D2F405689FD6848DAEEAD5D7C415701FB5D354DF305F00197435853A9D8EA655F37FA0598E637EBAD554CACC46301B520B6E80E91432529C0AA4D615CC8BC9E1E8EB5699CDAD1E7775FC9AB6DA5FC3ED1833E3D0FB1CD90BF8EE746747EC40F4E5542CDCF725BA998125B0E44CC8FAF33DCB6CA63E7A2840934935AD61133F44CEDDEEF3EFCEAD8D658B20A2CFD3191EB96C5555F3823F740590807005F8A1BE70DF84774391AB32374CDE7490000FE1F0F25000000000000000000000000000000000000000000000000000000000016FC00962E03F0",
                    "0x30750000789CEDD8DB728320140040F8FF9FEE4CA2C8CD0419E2B4CDEE83A9040E0804A121BE15DE6749590BCFA28F3FAA78A1BC8D79993D52961A8B8FBE23CA496B66C591DAFBED89657B524A9BB5E8E7323D649DB6B722367D7AB56D970A65A3568CD6A21EDEEA78553943C6FA2A8672E826BBF7A4D8ED83D5AFB04D3DF9F5ADAA70A9D1291F7B792F94ED563C388586E29189CF8B8EFACDB6CDC73656BDEF6F6ED080B4BBD877092B3740A992F411ABA4E5EEE8E234C0F617FC099F9AA8ED5AF10F7E10D5996A5BD39BF3E6823AF6EE8BEDCE7A69150327F1BA404C41D2A1BB8A5C55D20498B5284C11B23D4F67DFEDD3B869C731BF8F937C4CD770D667DD016882C7728A6D017BB9E646FCE4514355F3ABDCA9549DD2B41C2AE6C7D7C996AD0551AA093492DA9D6F9DF56FFF26DDE68B5EFEBFB03274BDD529D7C7EEB384C9535FF544C5EB6B221CC097B27076BD7B5FAEEDB2EC3DFAB80DC7F572A875ADBA2930008CF12A0200000000000000000000000000000000000080093FB6CB03DB",
                    "0x30750000789CEDD8096EC320100040EFFF3F5DA9896D16707D944439662A350D310B850D064FB16BDABF64B934B955FDFDA38A37E5B751D6992315A5915EFAD6281BBDB92A8EB4DEEF4FE4FE2C25EDA5699C73F9540CDADC8B68C6F46CDF4E552A662DCDD6A011BEB7F157E31C726CAC62CA5377717837AA3D7DB2FA0DB6A51BDFBE510D0E7534E5A377ED89BADD860FA6D0A178EF62EC3D63AB91F9D75B0FD5878B58D7D16E9ABFE0E42DBB8B7997F088645EF74151150DF79C21BE4FB1FD056FE15189DAAE151FF085A8CE54312DA79771C3980F60F1887B7B3EE5C58EBAC27A005D0EDD55D4F276D10B70D5A03029647B9E2E3E9BD3B8E9C79ADFF35385287E6E2F4DAC8D096882474EB15B8DF679C7998CDB1F86624E772D5F84625330776BF493123E8EFCF83AC5B235204A9540474ABBF9D659FFE64F96B7E5A2573E0BCBA1EBAD4E5E1FBBFFCB74F1D4979BCDF7DE0BE100BE9485F305D4B7E1FF841AD22100783DEE710000000000000000000000000000000000000070C10F29ED03DE",
                    "0x30750000789CEDD8DB8EC220100050E6FF7F7A13774B9942151BD6183DE7C10B9619048A608987CAE34BEAA5C96FD5DB8B43BC92DF465B678BD494467A1ADBA39CB4E6AA98C93E6E4FE4F6D492FED2D4CFB9BC349DB6B522BA3E7DB66D4F556A462D8DD6A21EFECB712F3953E6FA2A4A1EBA8BDD7B52EDE583354ED8979EDC7DAB122E353BE56374ED1375878927A7D0543C1AB71EB29ABDB76DC7727B7DF6F99BA9BB8B6D97B072035493D4A738142DF78A15767F78C71185A3FF9AA8FD5AF10137C4E14C15A59E5ED675633E8045BFB35E9A62E2247EAC1035483D74DF4FD205B86A519814B23F4F379F6DD3B86BC73EBFF7937CD4C772D667C301E882479E62C33AF95476DAC5B33DB6A798A9516F846653B0E55BFD4F091FC7FCF83ACD7AB420CA6102CD940EE7DB60FDDB3EA96FDB45AFFD2F2C873E6E75F2FA38FC2EE5E2A92FA78DF4F375211CC097B270BE818547062309C0A7F21B070000000000000000000000000000000000000017FC00C3B703DE"
            };
            String clearQuest = clearQuests[job];
            String questNotify = "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000";
            String specificSection = "0xA82F0000789CEDD3370A02611445E179D889B1356741B1300710145130A0829B70FF2B1057207AC0E1F1DFAF98EE1433EF4E14C5C60ACFF7F3C7B848E21289CB244E90B802E2F85895BC738DC475123748DC74792AA72C494ED522719BC41D8DC4094B913BA7499C217196C43912E7B5EDAF58977CED1E89FB719DCA061AC9FFD8908C6444E23189271A89133625779E91784EE2058997245E69DB4ED85AA70A816DC8FFBC25F14E0B0B81EDC9480E243E6A6121B01319C999C4172D2C04762523B96924F281DDC9C21E5A98888888888888888878F202ADDA1D05";
            String generalSection = "0xAE100000789CEDD2490E82401484E157C6AB12BD9E5B075C0B720B82A238808243043DC49F34E948EDBF74BA5E99D28999C9DC471978593B82F704E77D15E66974206D1F092E083E117C26F842F095E0D2CB6DAB227FBE117C27B826B821F8E1E59DFB8B9EA4ED17C16F826701C09F6124EEA296DCB923F84BB04D0116C12382C7007B1ACD49610B829704AF080E095EFFDF4850B4216D4704C7046F094E869138CB0F2D2A32EC";
            String despair = "0x981C0000789CEDC13101000000C2A0F54F6D0A3FA000000000000000BE061C980001";
            this.jdbcTemplate.update("update taiwan_cain.new_charac_quest set clear_quest=?,quest_notify=?,play_1=0,play_1_trigger=0,play_2=0,play_2_trigger=0,play_3=0,play_3_trigger=0,play_4=0,play_4_trigger=0,play_5=0,play_5_trigger=0,play_6=0,play_6_trigger=0,play_7=0,play_7_trigger=0,play_8=0,play_8_trigger=0,play_9=0,play_9_trigger=0,play_10=0,play_10_trigger=0,auto_clear=0,play_11=0,play_11_trigger=0,play_12=0,play_12_trigger=0,play_13=0,play_13_trigger=0,play_14=0,play_14_trigger=0,play_15=0,play_15_trigger=0,play_16=0,play_16_trigger=0,play_17=0,play_17_trigger=0,play_18=0,play_18_trigger=0,play_19=0,play_19_trigger=0,play_20=0,play_20_trigger=0,urgentQuestIndex=-1 where charac_no=?", clearQuest, questNotify, characNo);
            this.jdbcTemplate.update("update taiwan_cain.charac_titlebook set specific_section=?,general_section=?,despair=? where charac_no=?", specificSection, generalSection, despair, characNo);
            this.jdbcTemplate.update("update taiwan_cain.charac_stat set add_slot_flag=3 where charac_no=?", characNo);
        }
    }

    public void addAvata(int characNo, int itemId, int abilityNo) {
        Integer slot = this.jdbcTemplate.queryForObject("select max(slot) from taiwan_cain_2nd.user_items where charac_no=? and slot>=10", new Object[]{characNo}, Integer.class);
        assert slot == null || slot >= 10;
        slot = slot == null ? 10 : slot + 1;
        this.jdbcTemplate.update("insert into taiwan_cain_2nd.user_items(charac_no,slot,it_id,expire_date,obtain_from,reg_date,ipg_agency_no,ability_no,jewel_socket) values (?,?,?,'9999-12-31 23:59:59',0,NOW(),concat('B11190213',lpad(concat('',?), 10,'0')),?,0x010000000000010000000000000000000000000000000000000000000000)", characNo, slot, itemId, characNo, abilityNo);
    }

    public void fillAvataSlots(int characNo) {
        Integer job = this.jdbcTemplate.queryForObject("select job from taiwan_cain.charac_info where charac_no=?", new Object[]{characNo}, Integer.class);
        Integer avataCount = this.jdbcTemplate.queryForObject("select count(ui_id) from taiwan_cain_2nd.user_items where charac_no=? and slot>=0 and slot<10", new Object[]{characNo}, Integer.class);

        assert job != null;
        assert avataCount != null && avataCount != 0;

        int[][] itemIds = new int[][]{
                {42601, 42603, 42605, 42609, 42611, 42615, 42607, 42613},
                {46601, 46603, 46605, 46609, 46611, 46615, 46607, 46613},
                {50601, 50603, 50605, 50609, 50611, 50615, 50607, 50613},
                {54601, 54603, 54605, 54609, 54611, 54615, 54607, 54613},
                {58601, 58603, 58605, 58609, 58611, 58615, 58607, 58613},
                {1690008, 1690009, 1690010, 1690012, 1690013, 1690015, 1690011, 1690014},
                {1810001, 1810003, 1810005, 1810009, 1810011, 1810015, 1810007, 1810013},
                {2050001, 2050003, 2050005, 2050009, 2050011, 2050015, 2050007, 2050013},
                {2170001, 2170003, 2170005, 2170009, 2170011, 2170015, 2170007, 2170013},
                {42601, 42603, 42605, 42609, 42611, 42615, 42607, 42613}
        };
        for (int i = 0; i < itemIds[job].length; ++i) {
            int itemId = itemIds[job][i];
            this.jdbcTemplate.update("insert into taiwan_cain_2nd.user_items(charac_no,slot,it_id,expire_date,obtain_from,reg_date,ipg_agency_no,ability_no,jewel_socket) values (?,?,?,'9999-12-31 23:59:59',0,NOW(),concat('B11190213', lpad(concat('', ?), 10, '0')),0,0x010000000000010000000000000000000000000000000000000000000000)", characNo, i, itemId, characNo);
        }
    }

    public Integer getUid(String accountName, String password) {
        return this.jdbcTemplate.queryForObject("select UID from d_taiwan.accounts where accountname=? and password=md5(?)", new Object[]{accountName, password}, Integer.class);
    }

    public void addAccount(String accountName, String password) {
        try {
            this.jdbcTemplate.queryForObject("select uid from d_taiwan.accounts where accountname=?", new Object[]{accountName}, Integer.class);
        } catch (Exception e) {
            Integer uid = this.jdbcTemplate.queryForObject("select ifnull((select max(UID) from d_taiwan.accounts), 0) + 1", Integer.class);
            this.jdbcTemplate.update("insert into d_taiwan.accounts (uid,accountname,password) VALUES (?,?,md5(?))", uid, accountName, password);
            this.jdbcTemplate.update("insert into d_taiwan.limit_create_character (m_id) VALUES (?)", uid);
            this.jdbcTemplate.update("insert into d_taiwan.member_info (m_id,user_id) VALUES (?,?)", uid, uid);
            this.jdbcTemplate.update("insert into d_taiwan.member_join_info (m_id) VALUES (?)", uid);
            this.jdbcTemplate.update("insert into d_taiwan.member_miles (m_id) VALUES (?)", uid);
            this.jdbcTemplate.update("insert into d_taiwan.member_white_account (m_id) VALUES (?)", uid);
            this.jdbcTemplate.update("insert into taiwan_login.member_login (m_id) VALUES (?)", uid);
            this.jdbcTemplate.update("insert into taiwan_billing.cash_cera (account,cera,mod_tran,mod_date,reg_date) VALUES (?,0,0,NOW(),NOW())", uid);
            this.jdbcTemplate.update("insert into taiwan_billing.cash_cera_point (account,cera_point,reg_date,mod_date) VALUES (?,0,NOW(),NOW())", uid);
            this.jdbcTemplate.update("insert into taiwan_cain_2nd.member_avatar_coin (m_id) VALUES (?)", uid);
        }
    }

    public void setPassword(String accountName, String password) {
        this.jdbcTemplate.update("update d_taiwan.accounts set password=md5(?) where accountname=?", password, accountName);
    }

    public List<Charac> getCharacters(int uid) {
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select charac_no,convert(unhex(hex(convert(charac_name using latin1))) using utf8) as name,lev from taiwan_cain.charac_info where m_id=? and delete_flag<>1;", uid);
        List<Charac> characters = new ArrayList<>(list.size());
        list.forEach((map) -> {
            int id = (Integer) map.get("charac_no");
            String name = (String) map.get("name");
            int level = (Integer) map.get("lev");
            characters.add(new Charac(id, name, level));
        });
        return characters;
    }
}
