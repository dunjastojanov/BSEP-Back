package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.model.AlarmRule;
import com.myhouse.MyHouse.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rule")
public class AlarmRuleController {

    @Autowired
    private AlarmRuleService alarmRuleService;

    @PostMapping
    public ResponseEntity<?> addRule(@RequestBody AlarmRule alarmRule) {
        return ResponseEntity.ok(alarmRuleService.addRule(alarmRule));
    }

    @GetMapping
    public ResponseEntity<?> getRules() {
        return ResponseEntity.ok(alarmRuleService.getRules());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable String id) {
        alarmRuleService.deleteRule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
