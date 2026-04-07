package com.example.medicalofficereservationsystem.api.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorScheduleDtos {
    public record DoctorScheduleCreateRequest(DayOfWeek dayOfWeek, LocalTime startTime,
                                              LocalTime endTime, Long doctorId) implements Serializable {};

    public record DoctorScheduleResponse(Long id, DayOfWeek dayOfWeek, LocalTime startTime,
                                         LocalTime endTime, Long doctorId) implements Serializable {};

    public record AvailabilitySLotResponse(LocalTime startTime, LocalTime endTime) implements Serializable {};
}
