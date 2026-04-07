package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.OfficeStatus;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

public class OfficeDtos {
    public record OfficeCreateRequest(String name, String location, LocalTime openingHour, LocalTime closingHour) implements Serializable{};

    public record OfficeUpdateRequest(String name, String location, OfficeStatus status,
                                      LocalTime openingHour, LocalTime closingHour,
                                      Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};

    public record OfficeResponse(Long id, String name, String location, OfficeStatus status,
                                 Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};

    public record OfficeOccupancyResponse(Long id, String name, String location, LocalTime openingHour, LocalTime closingHour,
                                          Double busyHours, Long availableHours,
                                          Long occupancyPercent) implements Serializable{};

}
