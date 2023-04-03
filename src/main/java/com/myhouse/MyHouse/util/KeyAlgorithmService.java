package com.myhouse.MyHouse.util;

import com.myhouse.MyHouse.model.crypto.KeyAlgorithmType;

import java.security.*;

public class KeyAlgorithmService {

    public static KeyPair generateKeyPair(KeyAlgorithmType keyAlgorithmType, int keySize) {
        try {
            /** proveriti kako funkcionise sa Digital Signature Algorithm
             - zakucan algoritam key type
             - zakucan key size
             */
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlgorithmType.name());
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(keySize, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
