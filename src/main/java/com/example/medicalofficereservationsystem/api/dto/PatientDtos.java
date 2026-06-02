package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.enums.PatientDoctorStatus;

import java.io.Serializable;
import java.util.Set;

public class PatientDtos {

    public record PatientCreateRequest(String fullName, String email, String phone) implements Serializable {};

    public record PatientUpdateRequest(String fullName, String email, String phone,
                                       PatientDoctorStatus status) implements Serializable {};

    public record PatientResponse(Long id, String fullName, String email, String phone,
                                  PatientDoctorStatus status, Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable {};

    public record PatientNoShowResponse(Long id, String fullName, Long noShowCount) implements Serializable {};

}
