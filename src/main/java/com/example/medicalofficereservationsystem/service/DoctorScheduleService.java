package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;


public interface DoctorScheduleService {
    DoctorScheduleResponse createDoctorSchedule(DoctorScheduleCreateRequest req);

    DoctorScheduleResponse getDoctorScheduleByDoctorId(Long doctorId);
}
