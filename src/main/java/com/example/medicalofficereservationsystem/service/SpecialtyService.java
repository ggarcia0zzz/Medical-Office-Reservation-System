package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.SpecialtyDtos.*;

import java.util.List;

public interface SpecialtyService {
    SpecialtyResponse createSpecialty(SpecialtyCreateRequest req);

    List<SpecialtyResponse> getAllSpecialties();
}
