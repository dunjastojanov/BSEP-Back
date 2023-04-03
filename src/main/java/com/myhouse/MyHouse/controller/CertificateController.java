package com.myhouse.MyHouse.controller;

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

@RestController
@RequestMapping(path = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity<?> createNewCertificate(@RequestBody NewCertificateDataDTO newCertificateDataDTO) {
        return ResponseEntity.ok(certificateService.createNewCertificate(newCertificateDataDTO));
    }


    @GetMapping
    public void loadKeyStore(@RequestParam String fileName) {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadKeyStore(fileName);
    }


    @GetMapping(path = "algo")
    public ResponseEntity<?> keyAlgo(@RequestBody CertificateRequestDTO certificateRequestDTO) {
        KeyAlgorithmService.generateKeyPair(KeyAlgorithmType.valueOf(certificateRequestDTO.getKeyAlgorithm()), certificateRequestDTO.getKeyLength());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "invalidate/{id}")
    public ResponseEntity<?> invalidate(@PathVariable String id){
        return ResponseEntity.ok(certificateService.invalidate(id));
    }

}
