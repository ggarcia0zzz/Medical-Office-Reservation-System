package com.example.medicalofficereservationsystem.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

public class SpecialtyDtos {

    public record SpecialtyCreateRequest(@NotBlank(message = "Es obligatorio el nombre de la especialidad") @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")String name,
                                         @Size(max = 300, message = "La descripción no puede superar los 300 caracteres") String description) implements Serializable{};

    public record SpecialtyResponse(Long id, String name, String description) implements Serializable{};

}
