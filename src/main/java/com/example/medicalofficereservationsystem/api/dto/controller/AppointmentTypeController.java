package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.AppointmentTypeDtos.*;
import com.example.medicalofficereservationsystem.service.AppointmentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class AppointmentTypeController {

    private final AppointmentTypeService service;

    @PostMapping("/appointmenttypes")
    public ResponseEntity<AppointmentTypeResponse> create(@Valid @RequestBody AppointmentTypeCreateRequest req,
                                                          UriComponentsBuilder uriBuilder) {
        var body = service.createAppointmentType(req);
        var location = uriBuilder.path("/api/appointmenttypes/{appointmentTypeId}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/appointmenttypes/{appointmentTypeId}")
    public ResponseEntity<AppointmentTypeResponse> get(@PathVariable long appointmentTypeId) {
        return ResponseEntity.ok(service.getAppointmentTypeById(appointmentTypeId));
    }

}
