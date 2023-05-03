package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.LoginDTO;
import com.myhouse.MyHouse.dto.RegistrationDTO;
import com.myhouse.MyHouse.dto.UserDTO;
import com.myhouse.MyHouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    private ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) {
        userService.createUser(registrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "login")
    private ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        UserDTO u = userService.loginUser(loginDTO);

        if (u != null) {
            return ResponseEntity.ok(u);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
