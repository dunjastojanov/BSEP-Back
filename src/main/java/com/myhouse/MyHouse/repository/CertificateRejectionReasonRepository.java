package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.CertificateRejectionReason;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRejectionReasonRepository extends MongoRepository<CertificateRejectionReason, String> {
}
