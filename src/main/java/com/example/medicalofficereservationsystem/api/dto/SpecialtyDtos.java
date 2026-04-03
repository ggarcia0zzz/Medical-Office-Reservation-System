package com.example.medicalofficereservationsystem.api.dto;

import java.io.Serializable;
import java.util.Set;

public class SpecialtyDtos {

    public record SpecialtyCreateRequest(String name, String description) implements Serializable{};

    public record SpecialtyResponse(Long id, String name, String description) implements Serializable{};

}
