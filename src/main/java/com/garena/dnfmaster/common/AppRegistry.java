package com.garena.dnfmaster.common;

import java.util.HashMap;
import java.util.Map;

public class AppRegistry {
    private static final Map<String, Object> hashMap = new HashMap<>();

    public static <T> void putValue(String key, T value) {
        hashMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key, Class<T> c) {
        // 如果不存在则返回null
        return (T) hashMap.get(key);
    }
}
