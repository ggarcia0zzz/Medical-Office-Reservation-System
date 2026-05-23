package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.entities.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public class DoctorDtos{
    public record DoctorCreateRequest(@NotBlank(message = "Es obligatorio el nombre completo")@Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres") String fullName, @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")String email, @NotBlank(message = "El número de licencia es obligatorio") @Pattern(regexp = "^LIC-[0-9]{3,10}$", message = "El número de licencia debe tener el formato LIC-XXXX") String licenseNumber)
            implements Serializable {};

    public record DoctorUpdateRequest( @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String fullName, @Email(message = "El email no tiene un formato válido") String email,
                                       @Pattern(regexp = "^LIC-[0-9]{3,10}$", message = "El número de licencia debe tener el formato LIC-XXXX")String licenseNumber,
                                      Long specialtyId) implements Serializable{};

    public record DoctorResponse(Long id, String fullName, String email, String licenseNumber,
                                 SpecialtyDtos.SpecialtyResponse specialty,
                                 Set<DoctorScheduleDtos.DoctorScheduleResponse> doctorSchedules,
                                 Set<AppointmentDtos.AppointmentResponse> appointments) implements Serializable{};

    public record DoctorProductivityResponse(Long id, String fullName, SpecialtyDtos.SpecialtyResponse specialty) implements Serializable{};

}
