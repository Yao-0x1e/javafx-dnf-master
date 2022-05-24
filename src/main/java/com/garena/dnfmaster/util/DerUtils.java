package com.garena.dnfmaster.util;


import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DerUtils {
    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private static final String ALGORITHM = "RSA";
    private static final String PROVIDER = "BC";

    public static PublicKey convertDERToPublicKey(byte[] encoded) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return factory.generatePublic(spec);
    }

    public static PrivateKey convertDERToPrivateKey(byte[] encoded) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return factory.generatePrivate(spec);
    }
}
