package com.example.medicalofficereservationsystem.api.dto.controller;


import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.service.PatientService;
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
public class PatientController {
    private final PatientService service;

    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientCreateRequest req,
                                                  UriComponentsBuilder uriBuilder){
        var body = service.createPatient(req);
        var location = uriBuilder.path("/api/patients/{patientId}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<PatientResponse> get(@PathVariable Long patientId){
        return ResponseEntity.ok(service.getPatientById(patientId));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getAll(){
        return ResponseEntity.ok(service.getAllPatients());
    }

    @PatchMapping("/patients/{patientId}")
    public ResponseEntity<PatientResponse> update(@PathVariable Long patientId,
                                                  @Valid @RequestBody PatientUpdateRequest req){
        return ResponseEntity.ok(service.updatePatient(patientId, req));
    }
}
