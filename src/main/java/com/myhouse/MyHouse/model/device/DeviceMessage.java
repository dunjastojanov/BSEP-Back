package com.myhouse.MyHouse.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("deviceMessages")
public class DeviceMessage {
    @Id
    private String id;
    @DBRef
    private Device deviceId;
    private LocalDateTime timestamp;
    private String content;
    private DeviceMessageType type;
    private int value;

    public DeviceMessage(Device deviceId, LocalDateTime timestamp, String content, DeviceMessageType type, int value) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.content = content;
        this.type = type;
        this.value = value;
    }
}
