package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.cerificate.CertificateRequestDTO;
import com.myhouse.MyHouse.dto.cerificate.RejectionReasonDTO;
import com.myhouse.MyHouse.model.CertificateRejectionReason;
import com.myhouse.MyHouse.model.CertificateRequest;
import com.myhouse.MyHouse.model.crypto.KeyAlgorithmType;
import com.myhouse.MyHouse.repository.CertificateRejectionReasonRepository;
import com.myhouse.MyHouse.repository.CertificateRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CertificateRequestService {
    @Autowired
    private CertificateRejectionReasonRepository certificateRejectionReasonRepository;

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;


    public CertificateRequest getCertificateRequestById(String id) {
        return certificateRequestRepository.findById(id).orElse(null);
    }

    public void createRequestForCertificate(CertificateRequestDTO certificateRequestDTO) {
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setEmail(certificateRequestDTO.getEmail());
        certificateRequest.setCountry(certificateRequestDTO.getCountry());
        certificateRequest.setResolved(false);
        certificateRequest.setOrganizationName(certificateRequestDTO.getOrganizationName());
        certificateRequest.setOrganizationUnit(certificateRequestDTO.getOrganizationUnit());
        certificateRequestRepository.save(certificateRequest);
    }

    public List<CertificateRequest> getAllCertificateRequests() {
        return certificateRequestRepository.findAll().stream().filter(certificateRequest -> !certificateRequest.isResolved()).toList();
    }

    public void rejectCertificateCreation(RejectionReasonDTO rejectionReasonDTO) {
        CertificateRequest certificateRequest = getCertificateRequestById(rejectionReasonDTO.getId());
        certificateRequest.setResolved(true);
        certificateRequestRepository.save(certificateRequest);

        CertificateRejectionReason certificateRejectionReason = new CertificateRejectionReason();
        certificateRejectionReason.setCertificateRequestId(certificateRequest.getId());
        certificateRejectionReason.setReason(rejectionReasonDTO.getReason());
        certificateRejectionReasonRepository.save(certificateRejectionReason);
    }

    public void resolveCertificate(CertificateRequest certificateRequest) {
        certificateRequest.setResolved(true);
        certificateRequestRepository.save(certificateRequest);
    }

    public CertificateRequest getCertificateRequest(String id) {
        Optional<CertificateRequest> optional = certificateRequestRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NoSuchElementException();
        }
        return optional.get();
    }
}
