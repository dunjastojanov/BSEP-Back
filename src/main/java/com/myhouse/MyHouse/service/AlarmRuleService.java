package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.AlarmRule;
import com.myhouse.MyHouse.repository.AlarmRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmRuleService {

    @Autowired
    private AlarmRuleRepository alarmRuleRepository;

    public AlarmRule addRule(AlarmRule alarmRule) {
        return alarmRuleRepository.save(alarmRule);
    }

    public List<AlarmRule> getRules() {
        return alarmRuleRepository.findAll();
    }

    public void deleteRule(String id) {
        alarmRuleRepository.deleteById(id);
    }
}
