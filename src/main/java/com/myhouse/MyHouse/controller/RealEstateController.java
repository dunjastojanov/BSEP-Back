package com.myhouse.MyHouse.controller;


import com.myhouse.MyHouse.logging.LogSuccess;
import com.myhouse.MyHouse.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/realestate")
public class RealEstateController {

    @Autowired
    private RealEstateService realEstateService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(realEstateService.getAll());
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllAsPage(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(realEstateService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(realEstateService.getById(id));
    }

    @PostMapping("/{name}")
    @LogSuccess(message = "Created real estate.")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<?> createRealEstate(@PathVariable String name) {
        return ResponseEntity.ok(realEstateService.createRealEstate(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @LogSuccess(message = "Deleted real estate.")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        realEstateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
