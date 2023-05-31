package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public List<Device> getAll() {
        return deviceRepository.findAll();
    }
}
