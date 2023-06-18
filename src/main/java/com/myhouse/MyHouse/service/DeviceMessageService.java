package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.NewDeviceMessageDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.model.RealEstateConfiguration;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.repository.DeviceMessageRepository;
import com.myhouse.MyHouse.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceMessageService {

    private final KieSession kieSession;
    private final DeviceMessageRepository deviceMessageRepository;
    private final DeviceRepository deviceRepository;
    private final RealEstateConfigurationService realEstateConfigurationService;
//    private final SimpMessagingTemplate simpMessagingTemplate;

    public void logMessageAndSendAlarms(NewDeviceMessageDto message) {
        Optional<Device> optional = deviceRepository.findById(message.deviceId());

        if (optional.isEmpty()) {
            throw new NotFoundException("Device with given id doesn't exist.");
        }

        DeviceMessage deviceMessage = deviceMessageRepository.save(new DeviceMessage(optional.get(),
                message.timestamp(), message.content(), message.type(), message.value()));

        kieSession.insert(deviceMessage);
        kieSession.fireAllRules();

        Collection<DeviceMessage> messages = (Collection<DeviceMessage>) kieSession.getObjects(new ClassObjectFilter(DeviceMessage.class));

        List<DeviceMessage> alarms = messages.stream().filter(m -> m.getType().equals(DeviceMessageType.ALARM)).toList();

        deviceMessageRepository.saveAll(alarms);
//        alarms.forEach(this::sendAlarmToOwnersAndResidentsOfRealEstate);
    }

    private List<Device> getUserDevices(User u) {
        List<RealEstate> userRealEstates = new ArrayList<>();
        userRealEstates.addAll(u.getOwnerRealEstateIds());
        userRealEstates.addAll(u.getResidentRealEstateIds());

        List<Device> userDevices = new ArrayList<>();
        userRealEstates.forEach(realEstate -> userDevices.addAll(realEstateConfigurationService.getDevicesByRealEstateId(realEstate.getId())));
        return userDevices;
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
                    .filter(deviceMessage -> deviceMessage.getTimestamp().after(java.sql.Timestamp.valueOf(from.get())) &&
                                             deviceMessage.getTimestamp().before(java.sql.Timestamp.valueOf(to.get())))
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

    public List<?> getAllMessagesByUser(String userId) {
        List<DeviceMessage> messages = new ArrayList<>();

        List<RealEstateConfiguration> ownerConfigurations =
                realEstateConfigurationService.getRealEstateConfigurationForUser(userId, Role.OWNER.name());
        List<RealEstateConfiguration> residentConfigurations =
                realEstateConfigurationService.getRealEstateConfigurationForUser(userId, Role.RESIDENT.name());

        messages.addAll(ownerConfigurations.stream()
                .flatMap(configuration -> configuration.getDevices().stream())
                .flatMap(device -> getMessagesByDevice(device.getId()).stream()).toList());

        messages.addAll(residentConfigurations.stream()
                .flatMap(configuration -> configuration.getDevices().stream())
                .flatMap(device -> getMessagesByDevice(device.getId()).stream()).toList());

        return messages;
    }

    public String getRealEstateIdFromDeviceMessage(DeviceMessage message) {

        List<RealEstateConfiguration> configs = realEstateConfigurationService.getAll().stream()
            .filter(config -> {
                Device d = message.getDeviceId();
                return config.getDevices().contains(d);
            })
            .toList();

        return configs.get(0).getRealEstate().getId();
    }

    public List<DeviceMessage> getUserAlarms(User u) {
        List<DeviceMessage> alarms = new ArrayList<>();
        List<RealEstateConfiguration> configs = new ArrayList<>();
        Set<RealEstate> realEstates = new HashSet<>();
        realEstates.addAll(u.getOwnerRealEstateIds());
        realEstates.addAll(u.getResidentRealEstateIds());


        realEstates.forEach(realEstate ->
                configs.add(
                        realEstateConfigurationService.getRealEstateConfigurationByRealEstateId(realEstate.getId())));

        configs.forEach(config ->
            alarms.addAll(deviceMessageRepository.findAllByType(DeviceMessageType.ALARM).stream()
                    .filter(deviceMessage -> config.getDevices().contains(deviceMessage.getDeviceId()))
                    .toList())
            );

        List<DeviceMessage> filteredAlarms = alarms.stream().filter(alarm -> !alarm.isAlarmSent()).toList();

        filteredAlarms.forEach(a -> a.setAlarmSent(true));
        deviceMessageRepository.saveAll(filteredAlarms);

        return filteredAlarms;
    }

//    private void sendAlarmToOwnersAndResidentsOfRealEstate(DeviceMessage alarm) {
//        String realEstateId = getRealEstateIdFromDeviceMessage(alarm);
//        simpMessagingTemplate.convertAndSendToUser(realEstateId, "/app/alarm/real-estate", alarm);
//    }
}
