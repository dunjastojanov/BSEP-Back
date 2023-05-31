package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.model.RealEstateConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RealEstateConfigurationRepository extends MongoRepository<RealEstateConfiguration, String> {

    Optional<RealEstateConfiguration> findFirstByRealEstate(RealEstate realEstate);

}
