package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceMessageRepository extends MongoRepository<DeviceMessage, String> {

    List<DeviceMessage> findAllByDeviceId(Device device);

    List<DeviceMessage> findAllByType(DeviceMessageType type);
}
