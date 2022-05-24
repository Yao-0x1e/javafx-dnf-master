package com.garena.dnfmaster.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Collections;

public class SecurityUtils {
    private static String readKey(String location) throws IOException {
        Resource resource = new ClassPathResource(location);
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public static RSAPrivateKey getPrivateKey() throws Exception {
        return (RSAPrivateKey) PemUtils.convertPEMToPrivateKey(readKey("rsa/privatekey.pem"));
    }

    public static RSAPublicKey getPublicKey() throws Exception {
        return (RSAPublicKey) PemUtils.convertPEMToPublicKey(readKey("rsa/publickey.pem"));
    }

    public static byte[] decrypt(String hexStr, RSAPrivateKey privateKey) {
        BigInteger integer = new BigInteger(hexStr, 16);
        BigInteger exp = privateKey.getPrivateExponent();
        BigInteger mod = privateKey.getModulus();
        byte[] bytes = integer.modPow(exp, mod).toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
        }
        return bytes;
    }

    public static String getToken(int uid) throws Exception {
        RSAPrivateKey privateKey = getPrivateKey();
        String s3 = "010101010101010101010101010101010101010101010101010101010101010155914510010403030101";
        String s2 = String.format("%08X", uid);
        String s1 = "01" + String.join("", Collections.nCopies(414, "F")) + "00";
        String hexStr = String.join("", s1, s2, s3);
        byte[] bytes = decrypt(hexStr, privateKey);
        return DatatypeConverter.printBase64Binary(bytes);
    }
}
