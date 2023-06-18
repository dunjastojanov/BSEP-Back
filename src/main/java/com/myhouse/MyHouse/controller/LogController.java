package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.model.Log;
import com.myhouse.MyHouse.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

}