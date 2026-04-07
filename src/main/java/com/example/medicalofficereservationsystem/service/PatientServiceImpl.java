package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.entities.Patient;
import com.example.medicalofficereservationsystem.repository.PatientRepository;
import com.example.medicalofficereservationsystem.service.Mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientCreateRequest req) {
        Patient p = patientMapper.toEntity(req);
        return patientMapper.toResponse(patientRepository.save(p));
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        var p = patientRepository.findById(id).orElseThrow();
        return patientMapper.toResponse(p);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientUpdateRequest uptRequest) {
        var patient = patientRepository.findById(id).orElseThrow();
        patientMapper.updateFromRequest(patient, uptRequest);
        return patientMapper.toResponse(patientRepository.save(patient));
    }
}
