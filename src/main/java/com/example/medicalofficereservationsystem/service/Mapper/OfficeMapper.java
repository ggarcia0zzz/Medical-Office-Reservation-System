package com.example.medicalofficereservationsystem.service.Mapper;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;
import com.example.medicalofficereservationsystem.entities.Office;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    Office toEntity(OfficeCreateRequest req);

    OfficeResponse toResponse(Office office);


    OfficeOccupancyResponse toOccupancyResponse(Office office, Double busyHours, Long availableHours, Long occupancyPercent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(@MappingTarget Office o, OfficeUpdateRequest req);

}
