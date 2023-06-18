package com.myhouse.MyHouse.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("devices")
public class Device {
    @Id
    private String id;
    private String name;
    private String description;
    private DeviceType type;

    public Device(String name, String description, DeviceType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}

