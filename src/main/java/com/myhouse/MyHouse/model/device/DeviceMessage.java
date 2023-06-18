package com.myhouse.MyHouse.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Timestamp("timestamp")
@Role(Role.Type.EVENT)
@Document("deviceMessages")
public class DeviceMessage {
    @Id
    private String id;
    @DBRef
    private Device deviceId;
    private Date timestamp;
    private boolean alarmSent = false;
    private String content;
    private DeviceMessageType type;
    private int value;

    public DeviceMessage(Device deviceId, Date timestamp, String content, DeviceMessageType type, int value) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.content = content;
        this.type = type;
        this.value = value;
    }
}
