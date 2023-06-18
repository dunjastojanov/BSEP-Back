package com.myhouse.MyHouse.model;

import com.myhouse.MyHouse.model.device.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("realEstateConfiguration")
public class RealEstateConfiguration {

    @Id
    private String id;

    @DBRef
    private RealEstate realEstate;

    @DBRef
    private List<Device> devices;
    private String filter;
    private Duration messageInterval;

    public RealEstateConfiguration(RealEstate realEstate, List<Device> devices, String filter, Duration messageInterval) {
        this.realEstate = realEstate;
        this.devices = devices;
        this.filter = filter;
        this.messageInterval = messageInterval;
    }
}
