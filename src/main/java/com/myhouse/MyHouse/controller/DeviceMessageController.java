package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.model.device.DeviceMessageType;
import com.myhouse.MyHouse.service.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @GetMapping(value = "/message/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllMessagesByType(
            @RequestParam Optional<DeviceMessageType> type,
            @RequestParam Optional<String> deviceId,
            @RequestParam Optional<String> content,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDateTime> from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDateTime> to
    ) {
        try {
            return ResponseEntity.ok(deviceMessageService.searchDeviceMessages(type, deviceId, content, from, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping(value = "{deviceId}/message")
    @PreAuthorize("hasAnyAuthority('admin:read', 'owner:read')")
    public ResponseEntity<?> getAllMessagesByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceMessageService.getMessagesByDevice(deviceId));
    }
}
