package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.AlarmRule;
import com.myhouse.MyHouse.repository.AlarmRuleRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmRuleService {

    @Autowired
    private AlarmRuleRepository alarmRuleRepository;
    @Autowired
    private KieSession kieSession;

    public AlarmRule addRule(AlarmRule alarmRule) {
        AlarmRule rule = alarmRuleRepository.save(alarmRule);
        kieSession.insert(rule);
        return rule;
    }

    public List<AlarmRule> getRules() {
        return alarmRuleRepository.findAll();
    }
}
