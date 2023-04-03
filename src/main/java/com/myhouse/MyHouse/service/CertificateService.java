package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.NewCertificateDataDTO;
import com.myhouse.MyHouse.model.CertificateInfo;
import com.myhouse.MyHouse.model.CertificateRequest;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.crypto.IssuerData;
import com.myhouse.MyHouse.model.crypto.SubjectData;
import com.myhouse.MyHouse.repository.CertificateInfoRepository;
import com.myhouse.MyHouse.util.CertificateGenerator;
import com.myhouse.MyHouse.util.KeyAlgorithmService;
import com.myhouse.MyHouse.util.KeyStoreManager;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateInfoRepository certificateInfoRepository;

    @Autowired
    private CertificateRequestService certificateRequestService;
    @Autowired
    private UserService userService;


    public Object createNewCertificate(NewCertificateDataDTO newCertificateDataDTO) {
        CertificateRequest certificateRequest = certificateRequestService.getCertificateRequestById(newCertificateDataDTO.getCertificateRequestId());
        SubjectData subjectData = createSubjectData(certificateRequest, newCertificateDataDTO);
        if (subjectData == null)
            return null;
        IssuerData issuerData = createIssuerData(newCertificateDataDTO.getParentCertificateAlias());
        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate x509Certificate = certificateGenerator.generateCertificate(subjectData, issuerData, newCertificateDataDTO.getExtensions());
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadDefaultKeyStore();
        keyStoreManager.write(certificateRequest.getEmail(), issuerData.getPrivateKey(), x509Certificate);
        keyStoreManager.saveKeyStore();
        return null;
    }

    private IssuerData createIssuerData(String parentCertificateAlias) {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        return keyStoreManager.readIssuerFromStore(parentCertificateAlias);
    }

    private SubjectData createSubjectData(CertificateRequest certificateRequest, NewCertificateDataDTO newCertificateDataDTO) {
        try {
            KeyPair keyPairSubject = KeyAlgorithmService.generateKeyPair(certificateRequest.getKeyAlgorithmType(), certificateRequest.getKeySize());
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(newCertificateDataDTO.getValidFrom());
            Date endDate = iso8601Formater.parse(newCertificateDataDTO.getValidTo());
            String serialNumber = createCertificateInfo(newCertificateDataDTO);
            X500NameBuilder builder = generateX500NameBuilder(certificateRequest);
            return new SubjectData(keyPairSubject.getPublic(), builder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createCertificateInfo(NewCertificateDataDTO newCertificateDataDTO) throws ParseException {
        SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = iso8601Formater.parse(newCertificateDataDTO.getValidFrom());
        Date endDate = iso8601Formater.parse(newCertificateDataDTO.getValidTo());
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setPulled(false);
        certificateInfo.setAlias(newCertificateDataDTO.getCertificateAlias());
        certificateInfo.setParentAlias(newCertificateDataDTO.getParentCertificateAlias());
        certificateInfo.setValidTo(endDate);
        certificateInfo.setValidFrom(startDate);
        certificateInfoRepository.save(certificateInfo);
        return certificateInfo.getId();
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

    public CertificateInfo invalidate(String id) {
        Optional<CertificateInfo> optional = certificateInfoRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NoSuchElementException();
        }
        CertificateInfo certificateInfo = optional.get();
        certificateInfo.setPulled(true);
        return certificateInfoRepository.save(certificateInfo);
    }
}
