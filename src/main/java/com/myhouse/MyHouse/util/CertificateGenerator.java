package com.myhouse.MyHouse.util;

import com.myhouse.MyHouse.model.crypto.CertificateExtensions;
import com.myhouse.MyHouse.model.crypto.IssuerData;
import com.myhouse.MyHouse.model.crypto.SubjectData;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;


@NoArgsConstructor
public class CertificateGenerator {

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, List<String> extensions) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Posto klasa za generisanje sertifikata ne moze da primi direktno privatni kljuc pravi se builder za objekat
            // Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            // Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifikata
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            // Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
            builder = builder.setProvider("BC");

            // Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            // Postavljaju se podaci za generisanje sertifikata
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            if(!extensions.isEmpty())
                addExtensions(certGen, extensions);

            // Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException |
                 CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void addExtensions(X509v3CertificateBuilder certGen, List<String> extensions) throws CertIOException {
        if (extensions.contains(CertificateExtensions.AUTHORITY_KEY_IDENTIFIER.name())) {
            AuthorityInformationAccess authorityInformationAccess = new AuthorityInformationAccess(AccessDescription.id_ad_ocsp,
                    new GeneralName(GeneralName.dNSName, "certificate"));
            certGen.addExtension(Extension.authorityKeyIdentifier, false, authorityInformationAccess);
        } if (extensions.contains(CertificateExtensions.BASIC_CONSTRAINTS.name())) {
            certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
        }  if (extensions.contains(CertificateExtensions.KEY_USAGE.name())) {
            certGen.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
        }  if (extensions.contains(CertificateExtensions.EXTENDED_KEY_USAGE.name())) {
            KeyPurposeId[] keyPurposeIds = new KeyPurposeId[]{KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth};
            certGen.addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(keyPurposeIds));
        }
    }
}
