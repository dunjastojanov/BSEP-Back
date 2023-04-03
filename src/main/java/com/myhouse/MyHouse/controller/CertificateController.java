package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.CertificateInfoDTO;
import com.myhouse.MyHouse.dto.CertificateRequestDTO;
import com.myhouse.MyHouse.dto.NewCertificateDataDTO;
import com.myhouse.MyHouse.model.crypto.KeyAlgorithmType;
import com.myhouse.MyHouse.service.CertificateService;
import com.myhouse.MyHouse.util.KeyAlgorithmService;
import com.myhouse.MyHouse.util.KeyStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStoreException;
import java.text.ParseException;

@RestController
@RequestMapping(path = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity<?> createNewCertificate(@RequestBody NewCertificateDataDTO newCertificateDataDTO) {
        return ResponseEntity.ok(certificateService.createNewCertificate(newCertificateDataDTO));
    }

    @GetMapping(path = "{alias}")
    public ResponseEntity<Boolean> verifyCertificate(@PathVariable String alias) {
        return ResponseEntity.ok(certificateService.verifyCertificate(alias));
    }


    @GetMapping
    public void loadKeyStore(@RequestParam String fileName) {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadKeyStore(fileName);
    }


    @PostMapping(path = "info")
    public void createCertificateInfo(@RequestBody CertificateInfoDTO certificateInfoDTO) throws ParseException {
        certificateService.createCertificateInfo(certificateInfoDTO);
    }


//    @GetMapping(path = "algo")
//    public ResponseEntity<?> keyAlgo(@RequestBody CertificateRequestDTO certificateRequestDTO) {
//        KeyAlgorithmService.generateKeyPair(KeyAlgorithmType.valueOf(certificateRequestDTO.getKeyAlgorithm()), certificateRequestDTO.getKeyLength());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


    @GetMapping(path = "alias")
    public ResponseEntity<?> getAllKeyStoreAlias() throws KeyStoreException {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadKeyStore("myHouseKeyStore.jks");
        return ResponseEntity.ok(keyStoreManager.aliases());
    }

    @GetMapping(path = "issuer")
    public ResponseEntity<?> getIssuerPrivateKey()  {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadKeyStore("myHouseKeyStore.jks");
        return ResponseEntity.ok(keyStoreManager.readPrivateKey("root"));
    }
}
