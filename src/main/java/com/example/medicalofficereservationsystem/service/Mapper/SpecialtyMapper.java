package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.SpecialtyDtos.*;
import com.example.medicalofficereservationsystem.entities.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    Specialty toEntity(SpecialtyCreateRequest specialtyCreateRequest);

    SpecialtyResponse toResponse(Specialty specialty);


}
