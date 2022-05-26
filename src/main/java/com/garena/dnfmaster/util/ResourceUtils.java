package com.garena.dnfmaster.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtils {

    private ResourceUtils() {
    }

    public static URL loadURL(String path) {
        return ResourceUtils.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return ResourceUtils.class.getResourceAsStream(name);
    }

}
