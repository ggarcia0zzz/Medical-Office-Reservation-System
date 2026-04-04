package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorDtos.*;
import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorCreateRequest req) {
        Doctor d = doctorMapper.toEntity(req);
        return doctorMapper.toResponse(doctorRepository.save(d));
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        return doctorMapper.toResponse(doctorRepository.findById(id).orElseThrow());
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorUpdateRequest uptRequest) {
        Doctor d = doctorRepository.findById(id).orElseThrow();
        doctorMapper.updateFromRequest(d, uptRequest);
        return doctorMapper.toResponse(doctorRepository.save(d));
    }
}
