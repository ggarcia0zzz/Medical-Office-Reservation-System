package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentTypeDtos.*;
import com.example.medicalofficereservationsystem.entities.AppointmentType;
import com.example.medicalofficereservationsystem.repository.AppointmentTypeRepository;
import com.example.medicalofficereservationsystem.service.Mapper.AppointmentTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AppointmentTypeServiceImpl implements AppointmentTypeService {
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final AppointmentTypeMapper appointmentTypeMapper;

    @Override
    public AppointmentTypeResponse createAppointmentType(AppointmentTypeCreateRequest req) {
        AppointmentType at = appointmentTypeMapper.toEntity(req);
        return appointmentTypeMapper.toResponse(appointmentTypeRepository.save(at));
    }

    @Override
    public AppointmentTypeResponse getAppointmentTypeById(Long id) {
        return appointmentTypeMapper.toResponse(appointmentTypeRepository.findById(id).orElseThrow());
    }
}
