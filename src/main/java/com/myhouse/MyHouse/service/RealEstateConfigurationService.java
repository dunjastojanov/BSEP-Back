package com.myhouse.MyHouse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhouse.MyHouse.dto.NewDeviceDto;
import com.myhouse.MyHouse.dto.NewRealEstateConfigurationDto;
import com.myhouse.MyHouse.dto.RealEstateConfigurationDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.model.RealEstateConfiguration;
import com.myhouse.MyHouse.model.Role;
import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.repository.DeviceRepository;
import com.myhouse.MyHouse.repository.RealEstateConfigurationRepository;
import com.myhouse.MyHouse.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RealEstateConfigurationService {
    private final DeviceRepository deviceRepository;
    private final RealEstateConfigurationRepository realEstateConfigurationRepository;
    private final RealEstateRepository realEstateRepository;
    private final UserService userService;

    public List<RealEstateConfiguration> getAll() {
        return realEstateConfigurationRepository.findAll();
    }

    public RealEstateConfiguration createRealEstateConfiguration(
            NewRealEstateConfigurationDto realEstateConfigurationDto) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(realEstateConfigurationDto.realEstateId());

        if (realEstate.isEmpty())
            throw new NotFoundException("Real estate with id " + realEstateConfigurationDto.realEstateId() + " not found");

        List<Device> devices = deviceRepository.saveAll(
                realEstateConfigurationDto.devices().stream()
                        .map(device -> new Device(Encode.forHtml(device.name()), Encode.forHtml(device.description()), device.type()))
                        .toList());

        RealEstateConfiguration realEstateConfiguration = realEstateConfigurationRepository.save(new RealEstateConfiguration(
                realEstate.get(),
                devices,
                realEstateConfigurationDto.filter(),
                Duration.of(realEstateConfigurationDto.messageIntervalDuration(),
                        realEstateConfigurationDto.messageIntervalUnit())
        ));

        boolean writeSuccess = updateOrCreateConfigurationFile(realEstateConfiguration);

        System.out.println("Json write success: " + writeSuccess);

        return realEstateConfiguration;
    }

    public RealEstateConfiguration getRealEstateConfigurationByRealEstateId(String realEstateId) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(realEstateId);

        if (realEstate.isEmpty())
            throw new NotFoundException("Real estate with id " + realEstateId + " not found");

        Optional<RealEstateConfiguration> maybeConfig = realEstateConfigurationRepository.findFirstByRealEstate(realEstate.get());

        if (maybeConfig.isEmpty())
            throw new NotFoundException("Real estate configuration of real estate id " + realEstateId + " not found");

        return maybeConfig.get();
    }
    public RealEstateConfiguration addDeviceToRealEstateConfiguration(String realEstateId, NewDeviceDto newDevice) {
        RealEstateConfiguration config = getRealEstateConfigurationByRealEstateId(realEstateId);

        Device device = deviceRepository.save(new Device(newDevice.name(), newDevice.description(), newDevice.type()));
        config.getDevices().add(device);
        RealEstateConfiguration realEstateConfiguration = realEstateConfigurationRepository.save(config);

        boolean writeSuccess = updateOrCreateConfigurationFile(realEstateConfiguration);

        return realEstateConfiguration;
    }

    public boolean removeDeviceFromRealEstateConfiguration(String realEstateId, String deviceId) {
        RealEstateConfiguration config = getRealEstateConfigurationByRealEstateId(realEstateId);
        Optional<Device> optional = deviceRepository.findById(deviceId);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Device not found.");
        }
        config.getDevices().remove(optional.get());
        deviceRepository.deleteById(deviceId);
        RealEstateConfiguration realEstateConfiguration = realEstateConfigurationRepository.save(config);

        boolean writeSuccess = updateOrCreateConfigurationFile(realEstateConfiguration);

        return true;
    }

    public RealEstateConfiguration updateRealEstateConfiguration(RealEstateConfigurationDto realEstateConfigurationDto) {
        Optional<RealEstateConfiguration> maybeConfig = realEstateConfigurationRepository.findById(realEstateConfigurationDto.id());

        if (maybeConfig.isEmpty())
            throw new NotFoundException("Real estate configuration with id " + realEstateConfigurationDto.id() + " not found");

        RealEstateConfiguration config = maybeConfig.get();

        config.setDevices(realEstateConfigurationDto.devices());
        config.setMessageInterval(Duration.of(realEstateConfigurationDto.messageIntervalDuration(),
                realEstateConfigurationDto.messageIntervalUnit()));
        config.setFilter(realEstateConfigurationDto.filter());
        RealEstateConfiguration realEstateConfiguration = realEstateConfigurationRepository.save(config);

        boolean writeSuccess = updateOrCreateConfigurationFile(realEstateConfiguration);

        return realEstateConfiguration;
    }

    public List<Device> getDevicesByRealEstateId(String realEstateId) {
        return getRealEstateConfigurationByRealEstateId(realEstateId).getDevices();
    }

    public RealEstateConfiguration getRealEstateConfigurationById(String id) {
        Optional<RealEstateConfiguration> maybeConfig = realEstateConfigurationRepository.findById(id);

        if (maybeConfig.isEmpty())
            throw new NotFoundException("Real estate configuration with id " + id + " not found");

        return maybeConfig.get();
    }

    public List<RealEstateConfiguration> getRealEstateConfigurationForUser(String userId, String type) {
        User user = userService.findById(userId);
        if (Objects.equals(type, Role.OWNER.name()) && user.getRoles().contains(Role.OWNER)) {
            return user.getOwnerRealEstateIds().stream().map(realEstate -> getRealEstateConfigurationByRealEstateId(realEstate.getId())).toList();
        }
        if (Objects.equals(type, Role.RESIDENT.name()) && user.getRoles().contains(Role.RESIDENT)) {
            return user.getResidentRealEstateIds().stream().map(realEstate -> getRealEstateConfigurationByRealEstateId(realEstate.getId())).toList();
        }
        return new ArrayList<>();
    }

    private boolean updateOrCreateConfigurationFile(RealEstateConfiguration config) {
        try {
            RealEstateConfigurationDto configJson = new RealEstateConfigurationDto(config);
            Files.createDirectories(Paths.get("./config/"));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("./config/config_" + configJson.id() + ".json"), configJson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteById(String id) {
        realEstateConfigurationRepository.deleteById(id);
    }
}
