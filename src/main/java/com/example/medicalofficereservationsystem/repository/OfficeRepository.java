package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office,Long> {

    Optional<Office> findByName(String name);
    List<Office> findAll();

}
