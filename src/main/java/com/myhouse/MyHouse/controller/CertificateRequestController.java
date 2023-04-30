package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.cerificate.CertificateRequestDTO;
import com.myhouse.MyHouse.dto.cerificate.RejectionReasonDTO;
import com.myhouse.MyHouse.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/request")
public class CertificateRequestController {

    @Autowired
    private CertificateRequestService certificateRequestService;

    @PutMapping(path = "{id}")
    public ResponseEntity<?> rejectCertificateCreation(@RequestBody RejectionReasonDTO rejectionReasonDTO) {
        certificateRequestService.rejectCertificateCreation(rejectionReasonDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createRequestForClientCertificate(@RequestBody CertificateRequestDTO certificateRequestDTO) {
        certificateRequestService.createRequestForCertificate(certificateRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllClientRequests() {
        return ResponseEntity.ok(certificateRequestService.getAllCertificateRequests());
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getClientRequest(@PathVariable String id) {
        return ResponseEntity.ok(certificateRequestService.getCertificateRequest(id));
    }

}
