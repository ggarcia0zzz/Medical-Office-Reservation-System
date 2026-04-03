package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.OfficeStatus;

import java.io.Serializable;
import java.util.Set;

public class OfficeDtos {
    public record OfficeCreateRequest(String name, String location) implements Serializable{};

    public record OfficeUpdateRequest(String name, String location, OfficeStatus status,
                                      Set<Appointment> appointments) implements Serializable{};

    public record OfficeResponse(Long id, String name, String location, OfficeStatus status,
                                 Set<Appointment> appointments) implements Serializable{};

}
