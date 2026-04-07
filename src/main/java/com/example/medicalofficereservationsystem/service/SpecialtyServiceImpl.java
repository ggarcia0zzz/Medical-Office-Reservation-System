package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.SpecialtyDtos.*;
import com.example.medicalofficereservationsystem.entities.Specialty;
import com.example.medicalofficereservationsystem.repository.SpecialtyRepository;
import com.example.medicalofficereservationsystem.service.Mapper.SpecialtyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @Transactional
    public SpecialtyResponse createSpecialty(SpecialtyCreateRequest req) {
        Specialty s = specialtyMapper.toEntity(req);
        return specialtyMapper.toResponse(specialtyRepository.save(s));
    }

    @Override
    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyRepository.findAll().stream().map(specialtyMapper::toResponse).toList();
    }
}
