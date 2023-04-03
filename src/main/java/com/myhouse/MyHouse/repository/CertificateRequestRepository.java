package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.CertificateRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRequestRepository extends MongoRepository<CertificateRequest, String> {
}
