package com.garena.dnfmaster.util;


import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class PemUtils {
    private final static String PUB_KEY_TYPE = "PUBLIC KEY";
    private final static String PRI_KEY_TYPE = "RSA PRIVATE KEY";

    private static byte[] getContent(String pem) throws IOException {
        StringReader stringReader = new StringReader(pem);
        PemReader pemReader = new PemReader(stringReader);
        PemObject pemObject = pemReader.readPemObject();
        return pemObject.getContent();
    }

    public static PublicKey convertPEMToPublicKey(String pem) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] der = getContent(pem);
        return DerUtils.convertDERToPublicKey(der);
    }

    public static PrivateKey convertPEMToPrivateKey(String pem) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] der = getContent(pem);
        return DerUtils.convertDERToPrivateKey(der);
    }
}
