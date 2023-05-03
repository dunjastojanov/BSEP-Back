package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.RealEstate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RealEstateRepository extends MongoRepository<RealEstate, String> {
}
