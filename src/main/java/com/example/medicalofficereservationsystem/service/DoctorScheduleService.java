package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos;
import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;

import java.util.List;


public interface DoctorScheduleService {
    DoctorScheduleResponse createDoctorSchedule(DoctorScheduleCreateRequest req);

    List<DoctorScheduleDtos.DoctorScheduleResponse> getDoctorScheduleByDoctorId(Long doctorId);
}
