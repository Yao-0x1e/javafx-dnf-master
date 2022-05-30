package com.garena.dnfmaster.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SqlNoDataSourceInspection")
public class MailRepo {
    @Autowired
    private JdbcTemplate jdbc;

    public Integer findMaxLetterId() {
        return jdbc.queryForObject("select max(letter_id) from taiwan_cain_2nd.postal", Integer.class);
    }

    /**
     * @param letterId        邮件编号
     * @param letterText      邮件文本内容
     * @param characNo        角色编号
     * @param itemId          物品编号
     * @param itemQuantity    物品数量
     * @param upgrade         强化等级
     * @param seperateUpgrade 锻造等级
     * @param amplifyOption   增幅属性（0-4依次表示无红字、体力、精神、力量、智力）
     * @param goldQuantity    金币数量
     * @param isAvata         是否为装扮邮件
     * @param isCreature      是否为宠物邮件
     */
    public int addMail(int letterId, String sender, String letterText, int characNo, int itemId, int itemQuantity, int upgrade, int seperateUpgrade, int amplifyOption, int goldQuantity, boolean isSealed, boolean isAvata, boolean isCreature) {
        int avataFlag = isAvata ? 1 : 0;
        int creatureFlag = isCreature ? 1 : 0;
        int sealFlag = isSealed ? 1 : 0;
        assert avataFlag + creatureFlag != 2;
        int rows = 0;
        rows += jdbc.update("insert into taiwan_cain_2nd.letter (letter_id,charac_no,send_charac_name,letter_text,reg_date,stat) values (?,?,?,?,NOW(),1)", letterId, characNo, sender, letterText);
        rows += jdbc.update("insert into taiwan_cain_2nd.postal (occ_time,send_charac_name,receive_charac_no,amplify_option,seperate_upgrade,seal_flag,item_id,add_info,upgrade,gold,letter_id,avata_flag,creature_flag,random_option,item_guid) values (NOW(),?,?,?,?,?,?,?,?,?,?,?,?,'','')", sender, characNo, amplifyOption, seperateUpgrade, sealFlag, itemId, itemQuantity, upgrade, goldQuantity, letterId, avataFlag, creatureFlag);
        return rows;
    }

    public int truncateMails() {
        return jdbc.update("truncate taiwan_cain_2nd.postal; truncate taiwan_cain_2nd.letter;");
    }
}
