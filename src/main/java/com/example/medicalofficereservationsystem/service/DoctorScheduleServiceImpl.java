package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import com.example.medicalofficereservationsystem.repository.DoctorScheduleRepository;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService{
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Override
    @Transactional
    public DoctorScheduleResponse createDoctorSchedule(DoctorScheduleCreateRequest req) {
        DoctorSchedule ds = doctorScheduleMapper.toEntity(req);
        return doctorScheduleMapper.toResponse(doctorScheduleRepository.save(ds));
    }

    @Override
    public DoctorScheduleResponse getDoctorScheduleByDoctorId(Long doctorId) {
        return doctorScheduleMapper.toResponse(doctorScheduleRepository.findById(doctorId).orElseThrow());
    }
}
