package com.myhouse.MyHouse.repository;

import com.myhouse.MyHouse.model.AlarmRule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlarmRuleRepository extends MongoRepository<AlarmRule, String> {
}
