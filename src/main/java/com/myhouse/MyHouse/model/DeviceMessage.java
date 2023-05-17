package com.myhouse.MyHouse.model;

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
}
