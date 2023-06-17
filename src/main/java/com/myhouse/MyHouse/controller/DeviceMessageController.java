package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.NewDeviceMessageDto;
import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.service.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/device/message")
@RequiredArgsConstructor
public class DeviceMessageController {

    private final DeviceMessageService deviceMessageService;

    @PostMapping
    @LogSuccess(message = "Device message added.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> addDeviceMessage(@RequestBody @Valid NewDeviceMessageDto message) {
        deviceMessageService.logMessage(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllMessages() {
        return ResponseEntity.ok(deviceMessageService.getAllMessages());
    }

    @GetMapping(value = "/{type}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllMessagesByType(@PathVariable DeviceMessageType type) {
        return ResponseEntity.ok(deviceMessageService.getMessagesByType(type));
    }

    @GetMapping(value = "/device/{deviceId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'owner:read')")
    public ResponseEntity<?> getAllMessagesByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceMessageService.getMessagesByDevice(deviceId));
    }

    @GetMapping(value = "/user/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'owner:read')")
    public ResponseEntity<?> getAllMessagesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(deviceMessageService.getAllMessagesByUser(userId));
    }
}
