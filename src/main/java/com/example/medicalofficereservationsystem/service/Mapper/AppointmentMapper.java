package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

     @Mapping(target = "patient", ignore = true)
     @Mapping(target = "doctor", ignore = true)
     @Mapping(target = "office", ignore = true)
     @Mapping(target = "appointmentType", ignore = true)
     @Mapping(target = "id", ignore = true)
     @Mapping(target = "status", ignore = true)
     Appointment toEntity(AppointmentCreateRequest req);

     @Mapping(target = "patientId", source = "patient.id")
     @Mapping(target = "doctorId", source = "doctor.id")
     @Mapping(target = "officeId", source = "office.id")
     @Mapping(target = "appointmentTypeId", source = "appointmentType.id")
     AppointmentResponse toResponse(Appointment appointment);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void cancelFromRequest(@MappingTarget Appointment appointment, AppointmentCancelRequest req);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void completionFromRequest(@MappingTarget Appointment appointment, AppointmentCompletionRequest req);

}
