package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.user.RegistrationDTO;
import com.myhouse.MyHouse.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
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
    ResponseEntity<?> updateUserRoles(@PathVariable String id, @RequestBody List<String> roles) {
        return ResponseEntity.ok(userService.updateUserRole(id, roles));
    }
    @PutMapping("/realestates/{role}/{id}")
    ResponseEntity<?> updateUserRealEstates(@PathVariable String id,@PathVariable String role, @RequestBody List<String> realEstateIds) {
        userService.updateUserRealEstates(id,role, realEstateIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            userService.createUser(registrationDTO);
        } catch (QrGenerationException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/register/verification/{token}")
    private ResponseEntity<String> verifyUserRegistration(@PathVariable String token) {
        return ResponseEntity.ok(userService.verifyUserRegistration(token));
    }


    @GetMapping("/mfa/setup/{userEmail}")
    private ResponseEntity<?> mfaSetup(@PathVariable String userEmail) throws QrGenerationException {
        return ResponseEntity.ok(userService.mfaSetup(userEmail));
    }
}
