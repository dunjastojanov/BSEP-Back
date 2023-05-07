package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.repository.InvalidTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvalidTokenService {

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    public void addInvalidToken(String jwt){

    }
}
