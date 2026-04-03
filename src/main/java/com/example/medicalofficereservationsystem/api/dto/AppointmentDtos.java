package com.example.medicalofficereservationsystem.api.dto;

import com.example.medicalofficereservationsystem.enums.AppointmentStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentDtos {
    public record AppointmentCreateRequest(LocalDate startAt, LocalDateTime endAt,
                                           Long patientId, Long doctorId, Long officeId,
                                           Long appointmentTypeId) implements Serializable{};

    public record AppointmentCancelRequest(Long id, String cancellationReason) implements Serializable{};

    public record AppointmentCompletionRequest(Long id, String observations) implements Serializable{};

    public record AppointmentUpdateRequest(Long id) implements Serializable{};

    public record AppointmentResponse(Long id, LocalDateTime startAt, LocalDateTime endAt, AppointmentStatus status,
                                      Long patientId, Long doctorId, Long officeId,
                                      Long appointmentTypeId) implements Serializable{};
}
