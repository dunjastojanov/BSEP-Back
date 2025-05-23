package com.myhouse.MyHouse.util;

import lombok.NoArgsConstructor;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

/**
 * Cita sertifikat iz fajla
 */

@NoArgsConstructor
public class CertificateReader {

    public static final String ROOT_CERTIFICATE_ALIAS = "root";


    private void readFromBase64EncFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private void readFromBinEncFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection c = cf.generateCertificates(fis);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Certificate cert = (Certificate) i.next();
            }
        } catch (FileNotFoundException | CertificateException e) {
            e.printStackTrace();
        }
    }

    public static String getPemFromCertAlias(String alias) throws IOException {
        KeyStoreManager keyStore = new KeyStoreManager();
        keyStore.loadDefaultKeyStore();
        Certificate cert = keyStore.readCertificate(alias);
        return certificateToPem(cert);
    }

    public static String certificateToPem(final Certificate cert) throws IOException {
        final StringWriter writer = new StringWriter();
        final JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(cert);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }
}
