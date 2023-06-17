package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.model.Log;
import com.myhouse.MyHouse.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

}