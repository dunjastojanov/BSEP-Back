package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.cerificate.NewCertificateDataDTO;
import com.myhouse.MyHouse.model.CertificateInfo;
import com.myhouse.MyHouse.model.CertificateRequest;
import com.myhouse.MyHouse.model.Organization;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.repository.CertificateInfoRepository;
import com.myhouse.MyHouse.util.CertificateReader;
import com.myhouse.MyHouse.util.KeyStoreManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CertificateInfoService {

    @Autowired
    private CertificateInfoRepository certificateInfoRepository;

    @Autowired
    private UserService userService;


    public String getCertificateSerialNumber() {
        return String.valueOf(certificateInfoRepository.findAll().size() + 1);
    }

    public void createCertificateInfo(NewCertificateDataDTO newCertificateDataDTO, CertificateRequest certificateRequest) throws ParseException {
        User user = userService.getUserByEmail(certificateRequest.getEmail());
        SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
        Date validFrom = iso8601Formater.parse(newCertificateDataDTO.getValidFrom());
        Date validTo = iso8601Formater.parse(newCertificateDataDTO.getValidTo());
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setSerialNumber(getCertificateSerialNumber());
        certificateInfo.setPulled(false);
        certificateInfo.setAlias(newCertificateDataDTO.getCertificateAlias());
        certificateInfo.setParentAlias(newCertificateDataDTO.getParentCertificateAlias());
        certificateInfo.setValidTo(validTo);
        certificateInfo.setValidFrom(validFrom);
        Organization issuedFor = new Organization();
        issuedFor.setOrganizationUnit(certificateRequest.getOrganizationUnit());
        issuedFor.setOrganizationName(certificateRequest.getOrganizationName());
        issuedFor.setCommonName(user.getName() + " " + user.getSurname());
        Organization issuedBy = getOrganizationFromParentAlias(newCertificateDataDTO.getParentCertificateAlias());
        certificateInfo.setIssuedFor(issuedFor);
        certificateInfo.setIssuedBy(issuedBy);
        certificateInfoRepository.save(certificateInfo);
    }

    public String createRootCertificateInfo(NewCertificateDataDTO newCertificateDataDTO, CertificateRequest certificateRequest) throws ParseException {
        User user = userService.getUserByEmail(certificateRequest.getEmail());
        SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
        Date validFrom = iso8601Formater.parse(newCertificateDataDTO.getValidFrom());
        Date validTo = iso8601Formater.parse(newCertificateDataDTO.getValidTo());
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setPulled(false);
        certificateInfo.setAlias(newCertificateDataDTO.getCertificateAlias());
        certificateInfo.setParentAlias(newCertificateDataDTO.getParentCertificateAlias());
        certificateInfo.setValidTo(validTo);
        certificateInfo.setValidFrom(validFrom);
        Organization issuedFor = new Organization();
        issuedFor.setOrganizationUnit(certificateRequest.getOrganizationUnit());
        issuedFor.setOrganizationName(certificateRequest.getOrganizationName());
        issuedFor.setCommonName(user.getName() + " " + user.getSurname());
        certificateInfo.setIssuedFor(issuedFor);
        certificateInfo.setIssuedBy(issuedFor);
        certificateInfo.setSerialNumber(getCertificateSerialNumber());
        certificateInfoRepository.save(certificateInfo);
        return certificateInfo.getId();
    }

    private Organization getOrganizationFromParentAlias(String parentCertificateAlias) {
        return certificateInfoRepository.findByAlias(parentCertificateAlias).getIssuedFor();
    }


    public CertificateInfo getCertificateByAlias(String alias) {
        return certificateInfoRepository.findByAlias(alias);
    }

    public boolean isCertificateChainSignatureInvalid(CertificateInfo certificateInfo) {
        Security.addProvider(new BouncyCastleProvider());
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadDefaultKeyStore();
        Certificate leafCertificate = keyStoreManager.readCertificate(certificateInfo.getAlias());
        Certificate parentCertificate = keyStoreManager.readCertificate(certificateInfo.getParentAlias());
        CertificateInfo parentCertificateInfo = getCertificateByAlias(certificateInfo.getParentAlias());
        Certificate rootCertificate = keyStoreManager.readCertificate(parentCertificateInfo.getParentAlias());
        try {
            parentCertificate.verify(rootCertificate.getPublicKey());
            leafCertificate.verify(rootCertificate.getPublicKey());
            return false;
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                 SignatureException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean isCertificateExpired(CertificateInfo certificateInfo) {
        Date now = new Date();
        return certificateInfo.getValidFrom().after(now) || certificateInfo.getValidTo().before(now);
    }


    public boolean isCertificatePulled(CertificateInfo certificateInfo) {
        return certificateInfo.isPulled();
    }

    public boolean isAnyCertificateFromCertificateChainPulledOrExpired(CertificateInfo certificateInfo) {
        CertificateInfo parentCertificate = getCertificateByAlias(certificateInfo.getParentAlias());
        CertificateInfo rootCertificate = getCertificateByAlias(CertificateReader.ROOT_CERTIFICATE_ALIAS);
        return parentCertificate.isPulled()
                || rootCertificate.isPulled()
                || isCertificateExpired(parentCertificate)
                || isCertificateExpired(rootCertificate);
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

    public List<CertificateInfo> getAll() {
        return certificateInfoRepository.findAll();
    }
}
