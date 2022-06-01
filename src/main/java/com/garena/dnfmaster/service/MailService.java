package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.constant.MailType;
import com.garena.dnfmaster.mapper.LetterMapper;
import com.garena.dnfmaster.mapper.PostalMapper;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.util.DialogUtils;
import com.garena.dnfmaster.util.ItemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MailService {
    @Autowired
    private PostalMapper postalMapper;
    @Autowired
    private LetterMapper letterMapper;

    @Transactional
    public void sendMails(List<Charac> characters, String commaSeperatedItemIds, String inputItemQuantity, String inputGold, String inputUpgrade, String inputSeperateUpgrade, int amplifyOption, boolean sealOption, MailType mailType) {
        Assert.isFalse(characters.isEmpty() || commaSeperatedItemIds.isEmpty(), "请选择至少一个角色和输入至少一种物品之后再进行发送邮件操作");
        int gold = Integer.parseInt(inputGold);
        Assert.isTrue(gold >= 0, "金币数量必须为非负整数");
        int upgrade = Integer.parseInt(inputUpgrade);
        Assert.checkBetween(upgrade, 0, 31, "强化等级不在合法的数值区间内");
        int seperateUpgrade = Integer.parseInt(inputSeperateUpgrade);
        Assert.checkBetween(seperateUpgrade, 0, 7, "增幅等级不在合法的数值区间内");
        int itemQuantity = Integer.parseInt(inputItemQuantity);
        Assert.isTrue(itemQuantity >= 0, "物品数量必须为非负整数");
        Assert.checkBetween(amplifyOption, 0, 4, "非法红字属性");
        Assert.notNull(mailType, "非法邮件类型");

        List<Integer> itemIds = ItemUtils.parseCommaSeperatedItemIds(commaSeperatedItemIds);
        int sealFlag = sealOption ? 1 : 0;
        int creatureFlag = mailType.equals(MailType.CREATURE) ? 1 : 0;
        int avataFlag = mailType.equals(MailType.AVATA) ? 1 : 0;

        String sender = "Garena";
        String letterText = "";
        Integer lastLetterId = letterMapper.findMaxLetterId();
        int letterId = lastLetterId == null ? 0 : lastLetterId + 1;
        for (Charac charac : characters) {
            int characNo = charac.getNo();
            for (Integer itemId : itemIds) {
                letterMapper.insert(letterId, characNo, sender, letterText);
                postalMapper.insert(sender, characNo, amplifyOption, seperateUpgrade, sealFlag, itemId, itemQuantity, upgrade, gold, letterId, avataFlag, creatureFlag);
                letterId++;
            }
        }
    }

    @Transactional
    public void clearMails() {
        postalMapper.deleteAll();
        letterMapper.deleteAll();
    }
}
