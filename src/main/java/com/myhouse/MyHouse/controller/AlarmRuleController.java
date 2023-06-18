package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.model.AlarmRule;
import com.myhouse.MyHouse.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rule")
public class AlarmRuleController {

    @Autowired
    private AlarmRuleService alarmRuleService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    @LogSuccess(message = "Added alarm rule.")
    public ResponseEntity<?> addRule(@RequestBody AlarmRule alarmRule) {
        return ResponseEntity.ok(alarmRuleService.addRule(alarmRule));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getRules() {
        return ResponseEntity.ok(alarmRuleService.getRules());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @LogSuccess(message = "Deleted alarm rule.")
    public ResponseEntity<?> deleteRule(@PathVariable String id) {
        alarmRuleService.deleteRule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
