package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    DoctorSchedule toEntity(DoctorScheduleCreateRequest req);

    DoctorScheduleResponse toResponse(DoctorSchedule doctorSchedule);
}
