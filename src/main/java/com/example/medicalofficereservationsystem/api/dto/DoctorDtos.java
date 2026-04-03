package com.example.medicalofficereservationsystem.api.dto;

import java.io.Serializable;
import java.util.Set;

public class DoctorDtos {
    public record DoctorCreateRequest(String fullName, String email, String licenseNumber) implements Serializable {};

    public record DoctorUpdateRequest(String fullName, String email, String licenseNumber,
                                      Long specialtyId) implements Serializable{};

    public record DoctorResponse(Long id, String fullName, String email, String licenseNumber,
                                 SpecialtyDtos.SpecialtyResponse specialty,
                                 Set<DoctorScheduleDtos.DoctorScheduleResponse> doctorSchedules,
                                 Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};
}
