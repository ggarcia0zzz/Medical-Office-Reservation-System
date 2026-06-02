package com.example.medicalofficereservationsystem.repositories;

import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.Specialty;
import com.example.medicalofficereservationsystem.enums.PatientDoctorStatus;
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
    @DisplayName("findBySpecialtyAndActive: devuelve solo doctores activos de la especialidad")
    void findBySpecialtyAndActive_OnlyActiveDoctorsInSpecialty() {

        // Doctor activo de cardiología si debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. Lopez").email("lopez@gmail.com").licenseNumber("LIC-001")
                .active(PatientDoctorStatus.ACTIVE)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor activo de cardiología si debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. Garcia").email("garcia@gmail.com").licenseNumber("LIC-002")
                .active(PatientDoctorStatus.ACTIVE)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor INACTIVO de cardiología que no debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr. James").email("inactivo@gmail.com").licenseNumber("LIC-003")
                .active(PatientDoctorStatus.INACTIVE)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // Doctor activo de neurología que no debe aparecer
        doctorRepository.save(Doctor.builder().fullName("Dr Luna").email("luna@gmail.com").licenseNumber("LIC-004")
                .active(PatientDoctorStatus.ACTIVE)
                .specialty(neurology)
                .createdAt(Instant.now())
                .build());

        // ✅ FIXED: Use the new method signature with enum parameter
        Set<Doctor> result = doctorRepository.findBySpecialtyIdAndActive(cardiology.getId(), PatientDoctorStatus.ACTIVE);

        assertThat(result).hasSize(2);
        // ✅ FIXED: Compare enum instead of expecting boolean
        assertThat(result).allMatch(doctor -> doctor.getActive() == PatientDoctorStatus.ACTIVE);
        assertThat(result).allMatch(d -> d.getSpecialty().getId().equals(cardiology.getId()));
    }

    @Test
    @DisplayName("findBySpecialtyAndActive: devuelve vacío cuando no hay doctores activos")
    void findBySpecialtyAndActive_ReturnEmptyWhenNoActiveDoctors() {

        // Solo doctor inactivo
        doctorRepository.save(Doctor.builder().fullName("Dr James").email("inactivo@gmail.com").licenseNumber("LIC-001")
                .active(PatientDoctorStatus.INACTIVE)
                .specialty(cardiology)
                .createdAt(Instant.now())
                .build());

        // ✅ FIXED: Use the new method signature
        Set<Doctor> result = doctorRepository.findBySpecialtyIdAndActive(cardiology.getId(), PatientDoctorStatus.ACTIVE);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findBySpecialtyAndActive: no retorna doctores de otras especialidades")
    void findBySpecialtyAndActive_shouldNotReturnDoctorsFromOtherSpecialties() {

        doctorRepository.save(Doctor.builder().fullName("Dr. Neuro").email("neuro@gmail.com").licenseNumber("LIC-001")
                .active(PatientDoctorStatus.ACTIVE)
                .specialty(neurology)
                .createdAt(Instant.now())
                .build());

        // ✅ FIXED: Use the new method signature
        Set<Doctor> result = doctorRepository.findBySpecialtyIdAndActive(cardiology.getId(), PatientDoctorStatus.ACTIVE);

        assertThat(result).isEmpty();
    }
}