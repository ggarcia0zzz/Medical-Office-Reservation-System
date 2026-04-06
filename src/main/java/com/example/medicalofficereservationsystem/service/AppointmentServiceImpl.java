package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import com.example.medicalofficereservationsystem.repository.AppointmentRepository;
import com.example.medicalofficereservationsystem.service.Mapper.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentCreateRequest req) {
        Appointment a = appointmentMapper.toEntity(req);
        return appointmentMapper.toResponse(appointmentRepository.save(a));
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentMapper.toResponse(appointmentRepository.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public AppointmentResponse confirmAppointment(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        a.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentMapper.toResponse(appointmentRepository.save(a));
    }

    @Override
    @Transactional
    public AppointmentResponse noShowAppointment(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        a.setStatus(AppointmentStatus.NO_SHOW);
        return appointmentMapper.toResponse(appointmentRepository.save(a));
    }

    @Override
    @Transactional
    public AppointmentResponse completeAppointment(Long id, AppointmentCompletionRequest comRequest) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        appointmentMapper.completionFromRequest(a, comRequest);
        return appointmentMapper.toResponse(appointmentRepository.save(a));
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long id, AppointmentCancelRequest cancelRequest) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        appointmentMapper.cancelFromRequest(a, cancelRequest);
        return appointmentMapper.toResponse(appointmentRepository.save(a));
    }
}
