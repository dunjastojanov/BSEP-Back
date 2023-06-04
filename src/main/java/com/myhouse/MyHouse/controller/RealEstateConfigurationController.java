package com.myhouse.MyHouse.controller;

import com.myhouse.MyHouse.dto.NewRealEstateConfigurationDto;
import com.myhouse.MyHouse.dto.RealEstateConfigurationDto;
import com.myhouse.MyHouse.dto.RealEstateDto;
import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.service.RealEstateConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class RealEstateConfigurationController {

    private final RealEstateConfigurationService realEstateConfigurationService;

    @PostMapping(path = "/configuration")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<?> createConfiguration(@RequestBody NewRealEstateConfigurationDto newRealEstateConfigurationDto) {
        try {
            return ResponseEntity.ok(new RealEstateConfigurationDto(realEstateConfigurationService.createRealEstateConfiguration(newRealEstateConfigurationDto)));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/configuration")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<RealEstateConfigurationDto>> getAllConfigurations() {
        return ResponseEntity.ok(realEstateConfigurationService.getAll().stream()
                .map(config -> new RealEstateConfigurationDto(config.getId(),
                        new RealEstateDto(config.getRealEstate().getId(), config.getRealEstate().getName()),
                        config.getDevices(), config.getFilter(), config.getMessageInterval().get(ChronoUnit.SECONDS),
                        ChronoUnit.SECONDS)).toList());
    }


    @GetMapping(path = "/configuration/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getConfigurationById(@PathVariable String id) {
        try {
        return ResponseEntity.ok(
                new RealEstateConfigurationDto(
                        realEstateConfigurationService.getRealEstateConfigurationById(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "{realEstateId}/configuration")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getConfigurationByRealEstateId(@PathVariable String realEstateId) {
        try {
            return ResponseEntity.ok(
                    new RealEstateConfigurationDto(
                            realEstateConfigurationService.getRealEstateConfigurationByRealEstateId(realEstateId)));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/configuration")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> updateRealEstateConfiguration(@RequestBody RealEstateConfigurationDto realEstateConfigurationDto) {
        try {
        return ResponseEntity.ok(realEstateConfigurationService.updateRealEstateConfiguration(realEstateConfigurationDto));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
