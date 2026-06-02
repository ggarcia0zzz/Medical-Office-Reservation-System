package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.AppointmentTypeDtos.*;

import java.util.List;

public interface AppointmentTypeService {
    AppointmentTypeResponse createAppointmentType(AppointmentTypeCreateRequest req);

    AppointmentTypeResponse getAppointmentTypeById(Long id);

    List<AppointmentTypeResponse> getAllAppointmentTypes();

}
