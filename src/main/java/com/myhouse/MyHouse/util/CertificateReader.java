package com.myhouse.MyHouse.util;

import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            // Cita sertifikat po sertifikat
            // Svaki certifikat je izmedju
            // -----BEGIN CERTIFICATE-----,
            // i
            // -----END CERTIFICATE-----.
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                System.out.println(cert.toString());
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
            // Ovde se vade svi sertifkati
            Collection c = cf.generateCertificates(fis);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Certificate cert = (Certificate) i.next();
                System.out.println(cert);
            }
        } catch (FileNotFoundException | CertificateException e) {
            e.printStackTrace();
        }

    }
}
