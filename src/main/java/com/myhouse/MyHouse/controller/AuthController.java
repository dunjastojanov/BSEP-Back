package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login")
    @LogSuccess(message = "Login successful.")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.createAuthenticationToken(loginDTO, httpServletResponse));
    }

    @GetMapping(path = "/logout")
    @LogSuccess(message = "Logout successful.")
    public void logout(HttpServletRequest request) {
        authService.storeJwtAsInvalid(request);
    }
}
