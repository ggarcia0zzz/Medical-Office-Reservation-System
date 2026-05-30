package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentDtos {
    public record AppointmentCreateRequest(@NotNull(message = "Es obligatoria la fecha y hora de inicio") LocalDateTime startAt, LocalDateTime endAt,
                                           @NotNull(message = "Es obligatorio el id del paciente") Long patientId, @NotNull(message = "Es obligatorio el id del doctor") Long doctorId,@NotNull(message = "Es obligatorio el id del consultorio") Long officeId,
                                           @NotNull(message = "Es obligatorio el id del tipo de cita") Long appointmentTypeId) implements Serializable{};

    public record AppointmentCancelRequest(Long id,@NotBlank(message="Debe ser obligatorio el motivo de cancelación") @Size(min = 5, max = 500, message = "El motivo de cancelación debe tener entre 5 y 500 caracteres") String cancellationReason) implements Serializable{};

    public record AppointmentCompletionRequest(Long id,@Size(max=1000,message = "Las observaciones no pueden tener mas de 1000 caracteres") String observations) implements Serializable{};

    public record AppointmentUpdateRequest(Long id) implements Serializable{};

    public record AppointmentResponse(Long id, LocalDateTime startAt, LocalDateTime endAt, AppointmentStatus status,
                                      Long patientId, Long doctorId, Long officeId,
                                      Long appointmentTypeId) implements Serializable{};
}
