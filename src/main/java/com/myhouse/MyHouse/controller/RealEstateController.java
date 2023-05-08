package com.myhouse.MyHouse.controller;


import com.myhouse.MyHouse.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/realestate")
public class RealEstateController {

    @Autowired
    private RealEstateService realEstateService;

    @GetMapping
    private ResponseEntity<?> getAll() {
        return ResponseEntity.ok(realEstateService.getAll());
    }

    @GetMapping("/page")
    private ResponseEntity<?> getAllAsPage(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(realEstateService.getAll(page, size));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(realEstateService.getById(id));
    }

    @PostMapping("/{name}")
    private ResponseEntity<?> createRealEstate(@PathVariable String name) {
        return ResponseEntity.ok(realEstateService.createRealEstate(name));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(@PathVariable String id) {
        realEstateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
