package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.repository.AlarmRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmRuleService {

    @Autowired
    private AlarmRuleRepository alarmRuleRepository;
}
