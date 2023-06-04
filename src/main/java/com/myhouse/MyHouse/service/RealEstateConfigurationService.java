package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.dto.NewDeviceDto;
import com.myhouse.MyHouse.dto.NewRealEstateConfigurationDto;
import com.myhouse.MyHouse.dto.RealEstateConfigurationDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.model.RealEstateConfiguration;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.repository.DeviceRepository;
import com.myhouse.MyHouse.repository.RealEstateConfigurationRepository;
import com.myhouse.MyHouse.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RealEstateConfigurationService {
    private final DeviceRepository deviceRepository;
    private final RealEstateConfigurationRepository realEstateConfigurationRepository;
    private final RealEstateRepository realEstateRepository;

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

        return realEstateConfigurationRepository.save(new RealEstateConfiguration(
                realEstate.get(),
                devices,
                realEstateConfigurationDto.filter(),
                Duration.of(realEstateConfigurationDto.messageIntervalDuration(),
                        realEstateConfigurationDto.messageIntervalUnit())
        ));
    }

    public RealEstateConfiguration getRealEstateConfigurationByRealEstateId(String realEstateId) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(realEstateId);

        if(realEstate.isEmpty())
            throw new NotFoundException("Real estate with id " + realEstateId + " not found");

        Optional<RealEstateConfiguration> maybeConfig = realEstateConfigurationRepository.findFirstByRealEstate(realEstate.get());

        if(maybeConfig.isEmpty())
            throw new NotFoundException("Real estate configuration of real estate id " + realEstateId + " not found");

        return maybeConfig.get();
    }

    public RealEstateConfiguration addDeviceToRealEstateConfiguration(String realEstateId, NewDeviceDto newDevice) {
        RealEstateConfiguration config = getRealEstateConfigurationByRealEstateId(realEstateId);

        Device device = deviceRepository.save(new Device(newDevice.name(), newDevice.description(), newDevice.type()));
        config.getDevices().add(device);
        realEstateConfigurationRepository.save(config);

        return config;
    }

    public boolean removeDeviceFromRealEstateConfiguration(String realEstateId, Device device) {
        RealEstateConfiguration config = getRealEstateConfigurationByRealEstateId(realEstateId);
        config.getDevices().remove(device);
        deviceRepository.delete(device);
        realEstateConfigurationRepository.save(config);
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
        realEstateConfigurationRepository.save(config);
        return config;
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
}
