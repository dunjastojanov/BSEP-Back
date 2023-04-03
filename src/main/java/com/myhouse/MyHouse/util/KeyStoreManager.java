package com.myhouse.MyHouse.util;

import com.myhouse.MyHouse.model.crypto.IssuerData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreManager {

    private KeyStore keyStore;
    private static final char[] KEYSTORE_PASSWORD = "password".toCharArray();
    private static final String KEYSTORE_FILEPATH = "keystore/myHouseKeyStore.jks";

    private static final String KEYSTORE_FOLDER_PATH = "keystore/";


    //TODO eventutalno mozemo postaviti da imamo zakucane alijase za ta 2 sertifikata
    // - napraviti end point koji vraca to nesto sta treba

    public KeyStoreManager() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     * <p>
     * // @param keyStoreFile - datoteka odakle se citaju podaci
     *
     * @param //alias - alias putem kog se identifikuje sertifikat izdavaoca
     *                //* @param password     - lozinka koja je neophodna da se otvori key store
     *                //* @param keyPass      - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     */
    public IssuerData readIssuerFromStore(String alias) {
        try {
            // Datoteka se ucitava
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEYSTORE_FILEPATH));
            keyStore.load(in, KEYSTORE_PASSWORD);
            in.close();
            // Iscitava se sertifikat koji ima dati alias
            Certificate cert = keyStore.getCertificate(alias);

            // Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, KEYSTORE_PASSWORD);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(issuerName, privKey);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException |
                 IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    public Certificate readCertificate(String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEYSTORE_FILEPATH));
            ks.load(in, KEYSTORE_PASSWORD);
            in.close();
            if (ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException |
                 IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */
    public PrivateKey readPrivateKey(String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEYSTORE_FILEPATH));
            ks.load(in, KEYSTORE_PASSWORD);
            in.close();
            if (ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, KEYSTORE_PASSWORD);
                return pk;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException |
                 IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void loadKeyStore(String fileName) {
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(KEYSTORE_FOLDER_PATH + fileName), KEYSTORE_PASSWORD);
            } else {
                // Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, KEYSTORE_PASSWORD);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultKeyStore() {
        try {
            keyStore.load(new FileInputStream(KEYSTORE_FILEPATH), KEYSTORE_PASSWORD);
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore() {
        try {
            keyStore.store(new FileOutputStream(KEYSTORE_FILEPATH), KEYSTORE_PASSWORD);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String alias, PrivateKey privateKey, Certificate certificate) {
        try {
            keyStore.setKeyEntry(alias, privateKey, KEYSTORE_PASSWORD, new Certificate[]{certificate});
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public java.util.Enumeration<String> aliases() throws KeyStoreException {
        return this.keyStore.aliases();
    }
}
