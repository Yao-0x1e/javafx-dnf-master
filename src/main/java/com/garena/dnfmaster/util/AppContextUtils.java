package com.garena.dnfmaster.util;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class AppContextUtils {
    private static ConfigurableApplicationContext applicationContext;
    private static DefaultListableBeanFactory beanFactory;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        AppContextUtils.applicationContext = applicationContext;
        AppContextUtils.beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
    }

    public static <T> void addBean(Class<T> c, T object) {
        beanFactory.registerSingleton(c.getCanonicalName(), object);
    }

    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }
}
