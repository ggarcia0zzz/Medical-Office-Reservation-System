package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.AppointmentTypeDtos.*;
import com.example.medicalofficereservationsystem.entities.AppointmentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentTypeMapper {
    AppointmentType toEntity(AppointmentTypeCreateRequest req);

    AppointmentTypeResponse toResponse(AppointmentType appointmentType);

}
