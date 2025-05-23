package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.user.RegistrationDTO;
import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int size,
                                     @RequestParam(required = false) String id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String surname,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) String role) {
        return ResponseEntity.ok(userService.getAll(page, size, id, name, surname, email, role));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('owner:read','resident:read','admin:read')")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @LogSuccess(message = "Deleted user.")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    @LogSuccess(message = "Updated user roles.")
    ResponseEntity<?> updateUserRoles(@PathVariable String id, @RequestBody List<String> roles) {
        return ResponseEntity.ok(userService.updateUserRole(id, roles));
    }
    @PutMapping("/realestates/{role}/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    @LogSuccess(message = "Updated user real estates.")
    ResponseEntity<?> updateUserRealEstates(@PathVariable String id,@PathVariable String role, @RequestBody List<String> realEstateIds) {
        userService.updateUserRealEstates(id,role, realEstateIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "register")
    @LogSuccess(message = "Registered user.")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDTO registrationDTO) {
        try {
            userService.createUser(registrationDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (QrGenerationException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/register/verification/{token}")
    @LogSuccess(message = "Verified user.")
    public ResponseEntity<String> verifyUserRegistration(@PathVariable String token) {
        return ResponseEntity.ok(userService.verifyUserRegistration(token));
    }


    @GetMapping("/mfa/setup/{userEmail}")
    public ResponseEntity<?> mfaSetup(@PathVariable String userEmail) throws QrGenerationException {
        return ResponseEntity.ok(userService.mfaSetup(userEmail));
    }
}
