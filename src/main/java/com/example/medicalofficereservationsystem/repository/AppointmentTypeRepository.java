package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType,Long> {
    Optional<AppointmentType> findByName(String name);

}
