package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.cerificate.CertificateRequestDTO;
import com.myhouse.MyHouse.dto.cerificate.RejectionReasonDTO;
import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/request")
public class CertificateRequestController {

    @Autowired
    private CertificateRequestService certificateRequestService;

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    @LogSuccess(message = "Certificate request rejected.")
    public ResponseEntity<?> rejectCertificateCreation(@RequestBody RejectionReasonDTO rejectionReasonDTO) {
        certificateRequestService.rejectCertificateCreation(rejectionReasonDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('owner:write','resident:write')")
    @LogSuccess(message = "Certificate request created.")
    public ResponseEntity<?> createRequestForClientCertificate(@RequestBody CertificateRequestDTO certificateRequestDTO) {
        certificateRequestService.createRequestForCertificate(certificateRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllClientRequests() {
        return ResponseEntity.ok(certificateRequestService.getAllCertificateRequests());
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getClientRequest(@PathVariable String id) {
        return ResponseEntity.ok(certificateRequestService.getCertificateRequest(id));
    }

}
