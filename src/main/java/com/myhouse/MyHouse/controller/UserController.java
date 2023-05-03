package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.user.LoginDTO;
import com.myhouse.MyHouse.dto.user.RegistrationDTO;
import com.myhouse.MyHouse.dto.LoginDTO;
import com.myhouse.MyHouse.dto.RegistrationDTO;
import com.myhouse.MyHouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    private ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int size,
                                     @RequestParam(required = false) String id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String surname,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) String role) {

        return ResponseEntity.ok(userService.getAll(page, size, id, name, surname, email, role));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/roles/{id}")
    ResponseEntity<?> updateUserRoles(@PathVariable String id, @RequestParam List<String> roles) {
        userService.updateUserRole(id, roles);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/realestates/{id}")
    ResponseEntity<?> updateUserRealEstates(@PathVariable String id, @RequestParam List<String> realEstateIds) {
        userService.updateUserRealEstates(id, realEstateIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) {
        userService.createUser(registrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "login")
    private ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.loginUser(loginDTO));
    }

}
