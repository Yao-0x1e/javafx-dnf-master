package com.garena.dnfmaster.app;

import java.io.InputStream;
import java.net.URL;

public class MFXAppResourcesLoader {

    private MFXAppResourcesLoader() {
    }

    public static URL loadURL(String path) {
        return MFXAppResourcesLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return MFXAppResourcesLoader.class.getResourceAsStream(name);
    }

}
