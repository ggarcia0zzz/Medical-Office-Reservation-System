package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos;
import com.example.medicalofficereservationsystem.api.dto.PatientDtos;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    DoctorProductivityReport getDoctorProductivityReport(Long doctorId);

    List<PatientDtos.PatientNoShowResponse> getPatientNoShowReport();

    List<OfficeDtos.OfficeOccupancyResponse> getOfficeOccupancyReport(LocalDateTime startDate, LocalDateTime endDate, int pageNumber, int pageSize);

}
