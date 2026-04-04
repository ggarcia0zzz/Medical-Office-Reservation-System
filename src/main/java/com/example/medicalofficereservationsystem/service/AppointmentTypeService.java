package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentTypeDtos.*;

public interface AppointmentTypeService {
    AppointmentTypeResponse createAppointmentType(AppointmentTypeCreateRequest req);

    AppointmentTypeResponse getAppointmentTypeById(Long id);
}
