package com.example.medicalofficereservationsystem.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class AppointmentTypeDtos {

    public record AppointmentTypeCreateRequest(@NotBlank(message = "Es obligatorio el nombre del tipo de citas")
                                               @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String name,
                                               @NotNull(message = "Es obligatoria la duración en minutos")
                                               @Min(value = 5, message = "La duración mínima es de 5 minutos")int durationMinutes,
                                               @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")String description) implements Serializable{};
    public record AppointmentTypeResponse(Long id, String name, int durationMinutes, String description) implements Serializable{};
}
