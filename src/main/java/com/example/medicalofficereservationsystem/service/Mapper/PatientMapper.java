package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;
import com.example.medicalofficereservationsystem.entities.Patient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id",  ignore = true)
    Patient toEntity(PatientCreateRequest req);

    PatientResponse toResponse(Patient patient);

    //Since patient doesnt have a noShowCount by itself, we have to send it. since they have the same name
    //Mapstruct maps it on its own
    PatientNoShowResponse toNoShowResponse(Patient patient, Long noShowCount);

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
    void updateFromRequest(@MappingTarget Patient patient, PatientUpdateRequest req);
}
