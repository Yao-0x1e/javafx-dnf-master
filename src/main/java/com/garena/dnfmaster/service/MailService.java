package com.garena.dnfmaster.service;

import cn.hutool.core.lang.Assert;
import com.garena.dnfmaster.constant.MailType;
import com.garena.dnfmaster.mapper.LetterMapper;
import com.garena.dnfmaster.mapper.PostalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MailService {
    @Autowired
    private PostalMapper postalMapper;
    @Autowired
    private LetterMapper letterMapper;

    @Transactional
    public void sendMails(int characNo, String commaSeperatedItemIds, String inputItemQuantity, String inputGold, String inputUpgrade, String inputSeperateUpgrade, int amplifyOption, boolean sealOption, MailType mailType) {
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

        String[] inputItemIds = commaSeperatedItemIds.split(",");
        List<Integer> itemIds = new ArrayList<>(inputItemIds.length);
        for (String inputItemId : inputItemIds) {
            int itemId = Integer.parseInt(inputItemId);
            itemIds.add(itemId);
            Assert.isTrue(itemId >= 0, "物品编号必须为非负整数");
        }
        Set<Integer> set = new HashSet<>(itemIds);
        Assert.equals(set.size(), itemIds.size(), "请检查是否存在重复的物品编号");

        int sealFlag = sealOption ? 1 : 0;
        int createFlag = mailType.equals(MailType.CREATURE) ? 1 : 0;
        int avataFlag = mailType.equals(MailType.AVATA) ? 1 : 0;

        String sender = "Garena";
        for (Integer itemId : itemIds) {
            // postalMapper.insert(sender, characNo,amplifyOption,seperateUpgrade,sealFlag,itemId,)
        }
    }

    @Transactional
    public void clearMails() {
        postalMapper.deleteAll();
        letterMapper.deleteAll();
    }
}
