package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.model.User;
import com.myhouse.MyHouse.model.device.DeviceMessage;
import com.myhouse.MyHouse.service.DeviceMessageService;
import com.myhouse.MyHouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final DeviceMessageService deviceMessageService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'owner:read', 'resident:read')")
    public ResponseEntity<List<DeviceMessage>> getAlarmsForUser(Authentication auth) {
        UserDetails ud = (UserDetails) auth.getPrincipal();
        User u = userService.getUserByEmail(ud.getUsername());
        return ResponseEntity.ok(deviceMessageService.getUserAlarms(u));
    }
}
