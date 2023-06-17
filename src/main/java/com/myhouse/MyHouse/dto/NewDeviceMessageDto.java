package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.device.DeviceMessageType;

import java.time.LocalDateTime;

public record NewDeviceMessageDto(String deviceId, LocalDateTime timestamp, String content, DeviceMessageType type) {
}
