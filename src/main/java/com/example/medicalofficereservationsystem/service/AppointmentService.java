package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentCreateRequest req);

    AppointmentResponse getAppointmentById(Long id);

    //Get /api/Appointments reads like get all appointments, but im unsure

    AppointmentResponse confirmAppointment(AppointmentUpdateRequest uptRequest);

    AppointmentResponse noShowAppointment(AppointmentUpdateRequest uptRequest);

    AppointmentResponse completeAppointment(AppointmentCompletionRequest comRequest);

    AppointmentResponse cancelAppointment(AppointmentCancelRequest cancelRequest);



}
