package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.PatientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public class PatientDtos {

    public record PatientCreateRequest(@NotBlank(message = "Es obligatorio el nombre completo")@Size(min = 2, max =100,message = "El nombre debe tener entre 2 y 100 caracteres") String fullName, @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido") String email, String phone) implements Serializable {};

    public record PatientUpdateRequest(@Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String fullName,@Email(message = "El email no tiene un formato válido") String email, String phone,
                                       PatientStatus status, Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable {};

    public record PatientResponse(Long id, String email, String phone,
                                  PatientStatus status, Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable {};

    public record PatientNoShowResponse(Long id, String fullName, Long noShowCount) implements Serializable {};

}
