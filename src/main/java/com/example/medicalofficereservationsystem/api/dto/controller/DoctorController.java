package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.DoctorDtos.*;
import com.example.medicalofficereservationsystem.service.DoctorService;
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
public class DoctorController {
    private final DoctorService service;

    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> create(@Valid @RequestBody DoctorCreateRequest req,
                                                 UriComponentsBuilder uriBuilder) {
        var body = service.createDoctor(req);
        var location = uriBuilder.path("/api/doctors/{doctorId}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<DoctorResponse> get(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getDoctorById(doctorId));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getAll() {
        return ResponseEntity.ok(service.getAllDoctors());
    }

    @PatchMapping("/doctors/{doctorId}")
    public ResponseEntity<DoctorResponse> update(@PathVariable Long doctorId,
                                                 @Valid @RequestBody DoctorUpdateRequest req) {
        return ResponseEntity.ok(service.updateDoctor(doctorId, req));
    }
}
