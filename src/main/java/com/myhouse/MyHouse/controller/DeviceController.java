package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.NewDeviceDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.device.Device;
import com.myhouse.MyHouse.service.DeviceService;
import com.myhouse.MyHouse.service.RealEstateConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class DeviceController {

    private final RealEstateConfigurationService realEstateConfigurationService;

    private final DeviceService deviceService;

    @GetMapping(path = "/{realEstateId}/device")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getDevicesByRealestateId(@PathVariable String realEstateId) {
        try {
            return ResponseEntity.ok(
                    realEstateConfigurationService.getDevicesByRealEstateId(realEstateId));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/{realEstateId}/device")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<?> addDeviceToRealEstateConfiguration(@PathVariable String realEstateId,
                                                 @RequestBody NewDeviceDto newDeviceDto) {
        try {
            return ResponseEntity.ok(
                    realEstateConfigurationService.addDeviceToRealEstateConfiguration(realEstateId, newDeviceDto));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{realEstateId}/device")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> removeDeviceFromRealEstateConfiguration(@PathVariable String realEstateId,
                                                                     @RequestBody Device device) {
        try {
            if (realEstateConfigurationService.removeDeviceFromRealEstateConfiguration(realEstateId, device))
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/device")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<Device>> getAllDevice() {
        return ResponseEntity.ok(deviceService.getAll());
    }
}
