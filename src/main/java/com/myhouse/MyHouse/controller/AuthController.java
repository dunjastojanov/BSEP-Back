package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    private ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.createAuthenticationToken(loginDTO, httpServletResponse));
    }
}
