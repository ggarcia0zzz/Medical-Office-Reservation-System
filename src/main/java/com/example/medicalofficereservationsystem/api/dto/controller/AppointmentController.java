package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;
import com.example.medicalofficereservationsystem.service.AppointmentService;
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
public class AppointmentController {
    private final AppointmentService service;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentCreateRequest req,
                                                      UriComponentsBuilder uriBuilder) {
        var body = service.createAppointment(req);
        var location = uriBuilder.path("/api/appointments/{appointmentId}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/apointments/{appointmentId}")
    public ResponseEntity<AppointmentResponse> get(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(service.getAppointmentById(appointmentId));
    }

    @PatchMapping("/appointments/{appointmentId}/confirm")
    public ResponseEntity<AppointmentResponse> confirm(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(service.confirmAppointment(appointmentId));
    }

    @PatchMapping("/appointments/{appointmentId}/noshow")
    public ResponseEntity<AppointmentResponse> noShow(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(service.noShowAppointment(appointmentId));
    }

    @PatchMapping("/appointments/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> complete(@PathVariable Long appointmentId,
                                                        @Valid @RequestBody AppointmentCompletionRequest req) {
        return ResponseEntity.ok(service.completeAppointment(appointmentId, req));
    }

    @PatchMapping("appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Long appointmentId,
                                                      @Valid @RequestBody AppointmentCancelRequest req) {
        return ResponseEntity.ok(service.cancelAppointment(appointmentId, req));
    }

}
