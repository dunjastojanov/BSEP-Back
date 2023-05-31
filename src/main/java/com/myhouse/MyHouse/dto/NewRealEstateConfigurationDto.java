package com.myhouse.MyHouse.dto;

import java.time.temporal.ChronoUnit;
import java.util.List;

public record NewRealEstateConfigurationDto(String realEstateId, String filter, Long messageIntervalDuration,
                                            ChronoUnit messageIntervalUnit, List<NewDeviceDto> devices) {
}
