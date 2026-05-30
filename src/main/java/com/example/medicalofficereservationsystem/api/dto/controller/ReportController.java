package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;
import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;
import com.example.medicalofficereservationsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService service;

    @GetMapping("/reports/doctorproductivity/{doctorId}")
    public ResponseEntity<DoctorProductivityReport> getDoctorProductivityReport(@PathVariable Long doctorId){
        return ResponseEntity.ok(service.getDoctorProductivityReport(doctorId));
    }

    @GetMapping("reports/patientnoshow")
    public ResponseEntity<List<PatientNoShowResponse>> getPatientNoShowReport(){
        return ResponseEntity.ok(service.getPatientNoShowReport());
    }

    @GetMapping("/reports/officeoccupancy")
    public ResponseEntity<List<OfficeOccupancyResponse>> getOfficeOccupancyReport(@RequestParam LocalDateTime startDate,
                                                                                  @RequestParam LocalDateTime endDate,
                                                                                  @RequestParam int pageNumber,
                                                                                  @RequestParam int pageSize){
        return ResponseEntity.ok(service.getOfficeOccupancyReport(startDate, endDate, pageNumber, pageSize));
    }

}
