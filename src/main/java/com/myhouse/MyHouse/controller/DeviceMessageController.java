package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.service.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceMessageController {

    private final DeviceMessageService deviceMessageService;

    @PostMapping(value = "/message")
    public ResponseEntity<?> addDeviceMessage(@RequestBody @Valid DeviceMessage message) {
        deviceMessageService.logMessage(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/message")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllMessages() {
        return ResponseEntity.ok(deviceMessageService.getAllMessages());
    }

    @GetMapping(value = "/message")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllMessagesByType(@RequestParam DeviceMessageType type) {
        return ResponseEntity.ok(deviceMessageService.getMessagesByType(type));
    }

    @GetMapping(value = "{deviceId}/message")
    @PreAuthorize("hasAnyAuthority('admin:read', 'owner:read')")
    public ResponseEntity<?> getAllMessagesByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceMessageService.getMessagesByDevice(deviceId));
    }
}
