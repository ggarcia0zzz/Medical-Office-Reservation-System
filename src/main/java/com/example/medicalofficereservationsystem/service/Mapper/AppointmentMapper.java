package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

     Appointment toEntity(AppointmentCreateRequest req);

     AppointmentResponse toResponse(Appointment appointment);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void cancelFromRequest(@MappingTarget Appointment appointment, AppointmentCancelRequest req);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void completionFromRequest(@MappingTarget Appointment appointment, AppointmentCompletionRequest req);

}
