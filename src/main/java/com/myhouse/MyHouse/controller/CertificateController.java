package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.cerificate.CertificateInfoDTO;
import com.myhouse.MyHouse.dto.cerificate.NewCertificateDataDTO;
import com.myhouse.MyHouse.service.CertificateService;
import com.myhouse.MyHouse.util.KeyStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStoreException;
import java.text.ParseException;

@RestController
@RequestMapping(path = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(certificateService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<?> createNewCertificate(@RequestBody NewCertificateDataDTO newCertificateDataDTO) {
        return ResponseEntity.ok(certificateService.createNewCertificate(newCertificateDataDTO));
    }

    @GetMapping(path = "{alias}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Boolean> verifyCertificate(@PathVariable String alias) {
        return ResponseEntity.ok(certificateService.verifyCertificate(alias));
    }
    @PostMapping(path = "distribute")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> distributeCert(@RequestParam String userEmail) {
        if(certificateService.distributeCertificate(userEmail))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
    @PostMapping(path = "info")
    @PreAuthorize("hasAuthority('admin:write')")
    public void createCertificateInfo(@RequestBody CertificateInfoDTO certificateInfoDTO) throws ParseException {
        certificateService.createCertificateInfo(certificateInfoDTO);
    }

    @GetMapping(path = "alias")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllKeyStoreAlias() throws KeyStoreException {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.loadKeyStore("myHouseKeyStore.jks");
        return ResponseEntity.ok(keyStoreManager.aliases());
    }

    @PutMapping(path = "invalidate/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> invalidate(@PathVariable String id){
        return ResponseEntity.ok(certificateService.invalidate(id));
    }

}
