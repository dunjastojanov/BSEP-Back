package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.RealEstateConfiguration;
import com.myhouse.MyHouse.model.device.Device;

import java.time.temporal.ChronoUnit;
import java.util.List;

public record RealEstateConfigurationDto(String id, RealEstateDto realEstate, List<Device> devices, String filter,
                                         Long messageIntervalDuration, ChronoUnit messageIntervalUnit) {
    public RealEstateConfigurationDto(RealEstateDto realEstate, List<Device> devices, String filter,
                                      Long messageIntervalDuration, ChronoUnit messageIntervalUnit) {
        this(null, realEstate, devices, filter, messageIntervalDuration, messageIntervalUnit);
    }

    public RealEstateConfigurationDto(RealEstateConfiguration realEstateConfiguration) {
        this(realEstateConfiguration.getId(),
                new RealEstateDto(realEstateConfiguration.getRealEstate()),
                realEstateConfiguration.getDevices(),
                realEstateConfiguration.getFilter(),
                realEstateConfiguration.getMessageInterval().get(ChronoUnit.SECONDS),
                ChronoUnit.SECONDS);
    }
}