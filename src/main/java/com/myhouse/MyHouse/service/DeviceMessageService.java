package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.NewDeviceMessageDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.RealEstateConfiguration;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.repository.DeviceMessageRepository;
import com.myhouse.MyHouse.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceMessageService {

    private final DeviceMessageRepository deviceMessageRepository;

    private final DeviceRepository deviceRepository;

    private final RealEstateConfigurationService realEstateConfigurationService;
    public void logMessage(@Valid NewDeviceMessageDto message) {
        Optional<Device> optional = deviceRepository.findById(message.deviceId());

        if (optional.isEmpty()) {
            throw new NotFoundException("Device with given id doesn't exist.");
        }

        deviceMessageRepository.save(new DeviceMessage(optional.get(), message.timestamp(), message.content(), message.type(), message.value()));
    }

    public List<DeviceMessage> getMessagesByDevice(String deviceId) {
        Optional<Device> maybeDevice = deviceRepository.findById(deviceId);

        if (maybeDevice.isEmpty())
            throw new NotFoundException("Device with id " + deviceId + " not found");

        return deviceMessageRepository.findAllByDeviceId(maybeDevice.get());
    }

    public List<DeviceMessage> getMessagesByType(DeviceMessageType type) {
        return deviceMessageRepository.findAllByType(type);
    }

    public List<DeviceMessage> getAllMessages() {
        return deviceMessageRepository.findAll();
    }

    public List<?> getAllMessagesByUser(String userId) {
        List<DeviceMessage> messages = new ArrayList<>();

        List<RealEstateConfiguration> ownerConfigurations = realEstateConfigurationService.getRealEstateConfigurationForUser(userId, Role.OWNER.name());
        List<RealEstateConfiguration> residentConfigurations = realEstateConfigurationService.getRealEstateConfigurationForUser(userId, Role.RESIDENT.name());

        messages.addAll(ownerConfigurations.stream()
                .flatMap(configuration -> configuration.getDevices().stream())
                .flatMap(device -> getMessagesByDevice(device.getId()).stream()).toList());

        messages.addAll(residentConfigurations.stream()
                .flatMap(configuration -> configuration.getDevices().stream())
                .flatMap(device -> getMessagesByDevice(device.getId()).stream()).toList());

        return messages;
    }
}
