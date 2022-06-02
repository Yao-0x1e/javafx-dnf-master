package com.garena.dnfmaster.util;

import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

public class ConcurrentUtils {
    private final static Future<Object> EMPTY_FUTURE = AsyncResult.forValue(new Object());

    public static Future<Object> emptyFuture() {
        return EMPTY_FUTURE;
    }
}
