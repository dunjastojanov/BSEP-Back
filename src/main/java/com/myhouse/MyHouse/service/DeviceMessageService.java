package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.repository.DeviceMessageRepository;
import com.myhouse.MyHouse.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceMessageService {

    private final DeviceMessageRepository deviceMessageRepository;

    private final DeviceRepository deviceRepository;

    public void logMessage(DeviceMessage message) {
        deviceMessageRepository.save(message);
    }

    public List<DeviceMessage> getMessagesByDevice(String deviceId) {
        Optional<Device> maybeDevice = deviceRepository.findById(deviceId);

        if (maybeDevice.isEmpty())
            throw new NotFoundException("Device with id " + deviceId + " not found");

        return deviceMessageRepository.findAllByDeviceId(maybeDevice.get());
    }
}
