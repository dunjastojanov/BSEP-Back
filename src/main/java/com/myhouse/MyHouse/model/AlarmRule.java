package com.myhouse.MyHouse.model;

import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.model.device.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("alarmRule")
public class AlarmRule {
    @Id
    String id;
    DeviceMessageType messageType;
    DeviceType deviceType;
    int value;
    int amountOfMessages;
}
