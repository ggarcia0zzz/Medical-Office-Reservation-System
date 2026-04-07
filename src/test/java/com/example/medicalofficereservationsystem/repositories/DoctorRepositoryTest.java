package com.example.medicalofficereservationsystem.repositories;


import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.Specialty;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.repository.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DoctorRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    private Specialty cardiology;
    private Specialty neurology;

    @BeforeEach
    void setUp() {
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();

        cardiology = specialtyRepository.save(Specialty.builder()
                .name("Cardiología")
                .description("Corazón")
                .build());

        neurology = specialtyRepository.save(Specialty.builder()
                .name("Neurología")
                .description("Sistema nervioso")
                .build());
    }

    @Test
    @DisplayName("findBySpecialtyAndActiveIsTrue: devuelve solo doctores activos de la especialidad")
    void findBySpecialtyAndActiveIsTrue_OnlyActiveDoctorsInSpecialty() {

        // Doctor activo de cardiología si debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. Lopez").email("lopez@gmail.com").licenseNumber("LIC-001")
                .active(true)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor activo de cardiología si debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. Garcia").email("garcia@gmail.com").licenseNumber("LIC-002")
                .active(true)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor INACTIVO de cardiología que no debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. James").email("inactivo@gmail.com").licenseNumber("LIC-003")
                .active(false)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor activo de neurología que no debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr Luna").email("luna@gmail.com").licenseNumber("LIC-004")
                .active(true)
                .specialty(neurology)
                .createdAt(Instant.now())
                .build());

        Set<Doctor> result = doctorRepository.findBySpecialtyAndActiveIsTrue(cardiology);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Doctor::isActive);
        assertThat(result).allMatch(d -> d.getSpecialty().getId().equals(cardiology.getId()));
    }

    @Test
    @DisplayName("findBySpecialtyAndActiveIsTrue: devuelve vacío cuando no hay doctores activos")
    void findBySpecialtyAndActiveIsTrue_ReturnEmptyWhenNoActiveDoctors() {

        // Solo doctor inactivo
        doctorRepository.save(Doctor.builder().fullName("Dr James").email("inactivo@gmail.com").licenseNumber("LIC-001")
                .active(false)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        Set<Doctor> result = doctorRepository.findBySpecialtyAndActiveIsTrue(cardiology);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findBySpecialtyAndActiveIsTrue: no retorna doctores de otras especialidades")
    void findBySpecialtyAndActiveIsTrue_shouldNotReturnDoctorsFromOtherSpecialties() {

        doctorRepository.save(Doctor.builder().fullName("Dr. Neuro").email("neuro@gmail.com").licenseNumber("LIC-001")
                .active(true)
                .specialty(neurology)
                .createdAt(Instant.now())
                .build());

        Set<Doctor> result = doctorRepository.findBySpecialtyAndActiveIsTrue(cardiology);

        assertThat(result).isEmpty();
    }



}

