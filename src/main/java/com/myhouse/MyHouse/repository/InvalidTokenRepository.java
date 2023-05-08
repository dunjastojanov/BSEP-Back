package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.InvalidToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidTokenRepository extends MongoRepository<InvalidToken,String> {
    Optional<InvalidToken> findByToken(String token);
}
