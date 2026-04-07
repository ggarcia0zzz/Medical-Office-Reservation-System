package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorDtos.*;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorCreateRequest req);
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getAllDoctors();
    DoctorResponse updateDoctor(Long id, DoctorUpdateRequest uptRequest);
}
