package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.device.DeviceMessageType;

import java.time.LocalDateTime;
import java.util.Date;

public record NewDeviceMessageDto(String deviceId, Date timestamp, String content, DeviceMessageType type, int value) {

}
