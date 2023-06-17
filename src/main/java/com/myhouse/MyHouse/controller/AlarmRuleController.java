package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/rule")
public class AlarmRuleController {

    @Autowired
    private AlarmRuleService alarmRuleService;
}
