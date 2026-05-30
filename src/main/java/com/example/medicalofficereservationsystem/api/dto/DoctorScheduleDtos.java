package com.example.medicalofficereservationsystem.api.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorScheduleDtos {
    public record DoctorScheduleCreateRequest(@NotNull(message = "Es obligatorio el dia de la semana")DayOfWeek dayOfWeek,
                                              @NotNull(message = "Es obligatoria la hora de inicio")LocalTime startTime,
                                              @NotNull(message = "Es obligatoria la hora de fin")LocalTime endTime,
                                              @NotNull(message = "Es obligatorio el id del doctor") Long doctorId) implements Serializable {};

    public record DoctorScheduleResponse(Long id, DayOfWeek dayOfWeek, LocalTime startTime,
                                         LocalTime endTime, Long doctorId) implements Serializable {};

    public record AvailabilitySLotResponse(LocalTime startTime, LocalTime endTime) implements Serializable {};
}
