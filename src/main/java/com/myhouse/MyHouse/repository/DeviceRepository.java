package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.device.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    @Override
    Optional<Device> findById(String s);
}
