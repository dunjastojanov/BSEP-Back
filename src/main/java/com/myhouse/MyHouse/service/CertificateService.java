package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.CertificateInfoDTO;
import com.myhouse.MyHouse.dto.NewCertificateDataDTO;
import com.myhouse.MyHouse.model.CertificateInfo;
import com.myhouse.MyHouse.model.CertificateRequest;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.crypto.IssuerData;
import com.myhouse.MyHouse.model.crypto.KeyAlgorithmType;
import com.myhouse.MyHouse.model.crypto.SubjectData;
import com.myhouse.MyHouse.repository.CertificateRejectionReasonRepository;
import com.myhouse.MyHouse.util.CertificateGenerator;
import com.myhouse.MyHouse.util.KeyAlgorithmService;
import com.myhouse.MyHouse.util.KeyStoreManager;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CertificateService {

    @Autowired
    private CertificateRequestService certificateRequestService;
    @Autowired
    private UserService userService;

    @Autowired
    private CertificateInfoService certificateInfoService;

    public Object createNewCertificate(NewCertificateDataDTO newCertificateDataDTO) {
        if (certificateInfoService.getCertificateByAlias(newCertificateDataDTO.getCertificateAlias()) != null) {
            return null;
        }
        CertificateRequest certificateRequest = certificateRequestService.getCertificateRequestById(newCertificateDataDTO.getCertificateRequestId());
        if (certificateRequest.isResolved()) {
            return null;
        }
        certificateRequestService.resolveCertificate(certificateRequest);
        SubjectData subjectData = createSubjectData(certificateRequest, newCertificateDataDTO);
        if (subjectData == null)
            return null;
        IssuerData issuerData = createIssuerData(newCertificateDataDTO.getParentCertificateAlias());
        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate x509Certificate = certificateGenerator.generateCertificate(subjectData, issuerData, newCertificateDataDTO.getExtensions());
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadDefaultKeyStore();
        keyStoreManager.write(newCertificateDataDTO.getCertificateAlias(), issuerData.getPrivateKey(), x509Certificate);
        keyStoreManager.saveKeyStore();
        return null;
    }

    private IssuerData createIssuerData(String parentCertificateAlias) {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        return keyStoreManager.readIssuerFromStore(parentCertificateAlias);
    }

    private SubjectData createSubjectData(CertificateRequest certificateRequest, NewCertificateDataDTO newCertificateDataDTO) {
        try {
            KeyPair keyPairSubject = KeyAlgorithmService.generateKeyPair(KeyAlgorithmType.RSA, 2048);
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(newCertificateDataDTO.getValidFrom());
            Date endDate = iso8601Formater.parse(newCertificateDataDTO.getValidTo());
            X500NameBuilder builder = generateX500NameBuilder(certificateRequest);
            certificateInfoService.createCertificateInfo(newCertificateDataDTO, certificateRequest);
            String serialNumber = certificateInfoService.getCertificateSerialNumber();
            return new SubjectData(keyPairSubject.getPublic(), builder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private X500NameBuilder generateX500NameBuilder(CertificateRequest certificateRequest) {
        User user = userService.getUserByEmail(certificateRequest.getEmail());
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getName() + " " + user.getSurname());
        builder.addRDN(BCStyle.SURNAME, user.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, user.getName());
        builder.addRDN(BCStyle.O, certificateRequest.getOrganizationName());
        builder.addRDN(BCStyle.OU, certificateRequest.getOrganizationUnit());
        builder.addRDN(BCStyle.C, certificateRequest.getCountry());
        builder.addRDN(BCStyle.E, certificateRequest.getEmail());
        builder.addRDN(BCStyle.UID, userService.getUserIdByEmail(certificateRequest.getEmail()));
        return builder;
    }

    public void distributeCertificate(String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        // TODO: add getting certificate by user email
        // TODO: add certificate file generation
        // TODO: send certificate email
    }
    public boolean verifyCertificate(String alias) {
        CertificateInfo certificateInfo = certificateInfoService.getCertificateByAlias(alias);
        if (certificateInfoService.isCertificateExpired(certificateInfo))
            return false;
        else if (certificateInfoService.isCertificatePulled(certificateInfo)) {
            return false;
        } else if (certificateInfoService.isAnyCertificateFromCertificateChainPulledOrExpired(certificateInfo))
            return false;
        else if (certificateInfoService.isCertificateChainSignatureInvalid(certificateInfo)) {
            return false;
        }
        return true;
    }

    public void createCertificateInfo(CertificateInfoDTO certificateInfoDTO) throws ParseException {
        NewCertificateDataDTO newCertificateDataDTO = new NewCertificateDataDTO();
        newCertificateDataDTO.setCertificateAlias(certificateInfoDTO.getAlias());
        newCertificateDataDTO.setParentCertificateAlias(certificateInfoDTO.getParentAlias());
        newCertificateDataDTO.setValidTo(certificateInfoDTO.getValidTo());
        newCertificateDataDTO.setValidFrom(certificateInfoDTO.getValidFrom());
        CertificateRequest certificateRequest = certificateRequestService.getCertificateRequestById("642a11c667e20170c316e502");
        certificateInfoService.createRootCertificateInfo(newCertificateDataDTO, certificateRequest);
    }
}
