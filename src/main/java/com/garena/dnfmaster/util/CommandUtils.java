package com.garena.dnfmaster.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommandUtils {
    public static String exec(String... cmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd);
        // process.waitFor();
        return IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
    }
}

