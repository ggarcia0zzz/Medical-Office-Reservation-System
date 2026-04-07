package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.DoctorDtos.*;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;
import com.example.medicalofficereservationsystem.entities.Doctor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "id",  ignore = true)
    Doctor toEntity(DoctorCreateRequest req);

    DoctorResponse toResponse(Doctor doctor);

    DoctorProductivityResponse toProductivityResponse(Doctor doctor);

    DoctorProductivityReport toProductivityReport(DoctorProductivityResponse doctorInfo,
                                                  Long completedAppointments, Long totalAppointments,
                                                  float productivityPercent);

    /*
     * BeanMapping is used to control the Mapping behavior of the entire bean,
     * Unlike mapping, who only controls the target attribute
     *
     * this makes it so all attributes that come as null are ignored during mapping
     *
     * Since this method, unlike all the previous ones, needs 2 parameters, MappingTarget is used
     * to define who is the source and the target of the operation
     * */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(@MappingTarget Doctor doctor, DoctorUpdateRequest req);
}
