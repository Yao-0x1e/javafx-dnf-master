package com.garena.dnfmaster.util;

import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemUtils {
    // 不允许传入空字符串
    public static List<Integer> parseCommaSeperatedItemIds(String commaSeperatedItemIds) {
        Assert.isFalse(commaSeperatedItemIds.strip().isEmpty(), "请选择至少一种物品后再进行操作");
        String[] inputItemIds = commaSeperatedItemIds.split(",");
        List<Integer> itemIds = new ArrayList<>(inputItemIds.length);
        for (String inputItemId : inputItemIds) {
            int itemId = Integer.parseInt(inputItemId);
            itemIds.add(itemId);
            Assert.isTrue(itemId >= 0, "物品编号必须为非负整数");
        }
        Set<Integer> set = new HashSet<>(itemIds);
        Assert.equals(set.size(), itemIds.size(), "请检查是否存在重复的物品编号");
        return itemIds;
    }
}
