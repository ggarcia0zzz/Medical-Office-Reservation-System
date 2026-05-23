package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class AvailabilitySlotController {

    private final AvailabilitySlotService service;

    @GetMapping("/availabilityslots/{doctorId}")
    public ResponseEntity<List<AvailabilitySLotResponse>> get(@PathVariable Long doctorId,
                                                              @RequestParam LocalDate date,
                                                              @RequestParam Long appointmentTypeId) {
        return ResponseEntity.ok(service.getAvailabilitySlotsFromDoctor(doctorId, date, appointmentTypeId));
    }
}
