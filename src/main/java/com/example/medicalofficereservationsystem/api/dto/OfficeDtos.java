package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.OfficeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

public class OfficeDtos {
    public record OfficeCreateRequest(@NotBlank(message = "Es obligatorio el nombre del consultorio") @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String name,
                                      @NotBlank(message = "Es obligatoria la ubicación del consultorio")
                                      @Size(max = 200, message = "La ubicación no puede superar los 200 caracteres")String location,
                                      @NotNull(message = "Es obligatoria la hora de apertura")LocalTime openingHour,@NotNull(message = "Es obligatoria la hora de cierre") LocalTime closingHour) implements Serializable{};

    public record OfficeUpdateRequest( @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String name,
                                       @Size(max = 200, message = "La ubicación no puede superar los 200 caracteres")String location, OfficeStatus status,
                                      LocalTime openingHour, LocalTime closingHour,
                                      Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};

    public record OfficeResponse(Long id, String name, String location, OfficeStatus status,
                                 Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};

    public record OfficeOccupancyResponse(Long id, String name, String location, LocalTime openingHour, LocalTime closingHour,
                                          Double busyHours, Long availableHours,
                                          Long occupancyPercent) implements Serializable{};

}
