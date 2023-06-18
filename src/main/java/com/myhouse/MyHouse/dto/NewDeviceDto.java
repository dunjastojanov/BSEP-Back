package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.device.DeviceType;

public record NewDeviceDto(String name, String description, DeviceType type) {
}
