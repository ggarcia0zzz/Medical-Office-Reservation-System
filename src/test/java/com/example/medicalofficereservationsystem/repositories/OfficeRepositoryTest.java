package com.example.medicalofficereservationsystem.repositories;


import com.example.medicalofficereservationsystem.entities.Office;
import com.example.medicalofficereservationsystem.enums.OfficeStatus;
import com.example.medicalofficereservationsystem.repository.OfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class OfficeRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private OfficeRepository officeRepository;

    @BeforeEach
    void setUp() {
        officeRepository.deleteAll();
    }

    @Test
    @DisplayName("findByName: retorna el consultorio cuando el nombre si existe")
    void findByName_whenNameExists() {

        officeRepository.save(Office.builder().name("Consultorio 1").location("Piso 1").
                openingHour(LocalTime.of(8, 0)).closingHour(LocalTime.of(18, 0)).
                status(OfficeStatus.AVAILABLE).createdAt(Instant.now()).build());

        Optional<Office> result = officeRepository.findByName("Consultorio 1");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Consultorio 1");
        assertThat(result.get().getLocation()).isEqualTo("Piso 1");
    }

    @Test
    @DisplayName("findByName: retorna vacío cuando el nombre no existe")
    void findByName_whenNameDoesNotExists() {

        Optional<Office> result = officeRepository.findByName("Consultorio Inexistente");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByName: no retorna otro consultorio con nombre diferente")
    void findByName_notReturnOfficeWithDifferentName() {

        // Given
        officeRepository.save(Office.builder().name("Consultorio 1").location("Piso 1").
                        openingHour(LocalTime.of(8, 0)).closingHour(LocalTime.of(18, 0)).
                        status(OfficeStatus.AVAILABLE).createdAt(Instant.now()).build());

        Optional<Office> result = officeRepository.findByName("Consultorio 2");

        assertThat(result).isEmpty();
    }

}