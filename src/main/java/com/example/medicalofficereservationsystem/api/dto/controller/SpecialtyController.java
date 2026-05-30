package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.SpecialtyDtos.*;
import com.example.medicalofficereservationsystem.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class SpecialtyController {

    private final SpecialtyService service;

    @PostMapping("/specialties")
    public ResponseEntity<SpecialtyResponse> create(@Valid @RequestBody SpecialtyCreateRequest req,
                                                         UriComponentsBuilder uriBuilder) {
        var body = service.createSpecialty(req);
        var location = uriBuilder.path("/api/specialties/{specialtyId}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyResponse>> getAll() {
        return ResponseEntity.ok(service.getAllSpecialties());
    }

}
