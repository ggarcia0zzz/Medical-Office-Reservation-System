package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.service.DoctorScheduleService;
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
public class DoctorScheduleController {
    private final DoctorScheduleService service;

    @PostMapping("/doctorschedules")
    public ResponseEntity<DoctorScheduleResponse> create(@Valid @RequestBody DoctorScheduleCreateRequest req,
                                                         UriComponentsBuilder uriBuilder) {
        var body = service.createDoctorSchedule(req);
        var location = uriBuilder.path("/api/doctorschedules/{scheduleId}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/doctorschedules/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponse>> getAll(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getDoctorScheduleByDoctorId(doctorId));
    }

}
