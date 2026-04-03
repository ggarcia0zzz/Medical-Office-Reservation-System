package com.example.medicalofficereservationsystem.api.dto;

import java.io.Serializable;

public class AppointmentTypeDtos {

    public record AppointmentTypeCreateRequest(String name, int durationMinutes, String description) implements Serializable{};
    public record AppointmentTypeResponse(Long id, String name, int durationMinutes, String description) implements Serializable{};
}
