package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.RegistrationVerification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationVerificationRepository extends MongoRepository<RegistrationVerification, String> {

    Optional<RegistrationVerification> findByToken(String token);
}
