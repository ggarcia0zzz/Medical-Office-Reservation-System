package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentCreateRequest req);

    AppointmentResponse getAppointmentById(Long id);

    List<AppointmentResponse> getAllAppointments();

    //Get /api/Appointments reads like get all appointments, but im unsure

    AppointmentResponse confirmAppointment(Long appointmentId);

    AppointmentResponse noShowAppointment(Long appointmentId);

    AppointmentResponse completeAppointment(Long id, AppointmentCompletionRequest comRequest);

    AppointmentResponse cancelAppointment(Long id, AppointmentCancelRequest cancelRequest);



}
