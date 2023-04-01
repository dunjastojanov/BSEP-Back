package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.CertificateInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateInfoRepository extends MongoRepository<CertificateInfo, String> {
}
