package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.repository.DeviceMessageRepository;
import com.myhouse.MyHouse.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<DeviceMessage> searchDeviceMessages(
            Optional<DeviceMessageType> type,
            Optional<String> deviceId,
            Optional<String> content,
            Optional<LocalDateTime> from,
            Optional<LocalDateTime> to
    ) {
        List<DeviceMessage> messages;

        if (deviceId.isEmpty()) {
            messages = deviceMessageRepository.findAll();
        } else {
            messages = getMessagesByDevice(deviceId.get());
        }

        if (type.isPresent()) {
            messages = messages.stream()
                    .filter(deviceMessage -> deviceMessage.getType().equals(type.get()))
                    .collect(Collectors.toList());
        }

        if (content.isPresent()) {
            messages = messages.stream()
                    .filter(deviceMessage -> deviceMessage.getContent().contains(content.get()))
                    .collect(Collectors.toList());
        }

        if (from.isPresent() && to.isPresent()) {
            messages = messages.stream()
                    .filter(deviceMessage -> deviceMessage.getTimestamp().isAfter(from.get()) &&
                                             deviceMessage.getTimestamp().isBefore(to.get()))
                    .collect(Collectors.toList());
        }

        return messages;
    }

    public List<DeviceMessage> getMessagesByType(DeviceMessageType type) {
        return deviceMessageRepository.findAllByType(type);
    }

    public List<DeviceMessage> getAllMessages() {
        return deviceMessageRepository.findAll();
    }
}
