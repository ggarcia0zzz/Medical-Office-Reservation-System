package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.entities.Patient;
import org.hibernate.sql.Update;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(PatientCreateRequest req);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    PatientResponse updatePatient(Long id, PatientUpdateRequest uptRequest);
}
