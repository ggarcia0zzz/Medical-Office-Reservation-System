package com.example.medicalofficereservationsystem.repositories;


import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import com.example.medicalofficereservationsystem.entities.Specialty;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.repository.DoctorScheduleRepository;
import com.example.medicalofficereservationsystem.repository.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DoctorScheduleRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    private Doctor doctor;
    private Doctor doctor2;

    @BeforeEach
    void setUp() {
        doctorScheduleRepository.deleteAll();
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();

        Specialty specialty = specialtyRepository.save(Specialty.builder()
                .name("Cardiología")
                .description("Especialidad del corazón")
                .build());

        doctor = doctorRepository.save(Doctor.builder().fullName("Dr Lopez").email("lopez@gmail.com").licenseNumber("LIC-001").active(true)
                .specialty(specialty)
                .createdAt(Instant.now())
                .build());

        doctor2 = doctorRepository.save(Doctor.builder().fullName("Dr Garcia").email("garcia@gmail.com").licenseNumber("LIC-002")
                .active(true)
                .specialty(specialty)
                .createdAt(Instant.now())
                .build());
    }

    private DoctorSchedule saveSchedule(Doctor doc, DayOfWeek day,
                                        LocalTime start, LocalTime end) {
        return doctorScheduleRepository.save(DoctorSchedule.builder()
                .doctor(doc)
                .dayOfWeek(day)
                .startTime(start)
                .endTime(end)
                .build());
    }


    @Test
    @DisplayName("findByDayOfWeekAndDoctor: retorna el horario cuando existe")
    void findByDayOfWeekAndDoctor_ReturnScheduleWhenExists() {

        saveSchedule(doctor, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));

        Optional<DoctorSchedule> result = doctorScheduleRepository
                .findByDayOfWeekAndDoctor_Id(DayOfWeek.MONDAY, doctor.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.get().getStartTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(result.get().getEndTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(result.get().getDoctor().getId()).isEqualTo(doctor.getId());
    }

    @Test
    @DisplayName("findByDayOfWeekAndDoctor: retorna vacío cuando el día no tiene horario")
    void findByDayOfWeekAndDoctor_sReturnEmptyWhenDayIsNotScheduled() {

        saveSchedule(doctor, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));

        Optional<DoctorSchedule> result = doctorScheduleRepository
                .findByDayOfWeekAndDoctor_Id(DayOfWeek.TUESDAY, doctor.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByDayOfWeekAndDoctor: no retorna horario de otro doctor")
    void findByDayOfWeekAndDoctor_DoesNotReturnOtherDoctorSchedule() {

        saveSchedule(doctor2, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(13, 0));

        Optional<DoctorSchedule> result = doctorScheduleRepository
                .findByDayOfWeekAndDoctor_Id(DayOfWeek.TUESDAY, doctor.getId());

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("findByDoctor_Id: retorna todos los horarios del doctor")
    void findByDoctorId_ReturnAllSchedulesForDoctor() {

        // doctor tiene 3 horarios
        saveSchedule(doctor, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));
        saveSchedule(doctor, DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0));
        saveSchedule(doctor, DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0));

        Set<DoctorSchedule> result = doctorScheduleRepository.findByDoctor_Id(doctor.getId());

        assertThat(result).hasSize(3);
        assertThat(result).extracting(DoctorSchedule::getDayOfWeek).containsExactlyInAnyOrder(
                        DayOfWeek.MONDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.FRIDAY);
    }

    @Test
    @DisplayName("findByDoctor_Id: retorna vacío cuando el doctor no tiene horarios")
    void findByDoctorId_ReturnEmptyWhenNoSchedules() {

        // When: doctor sin horarios guardados
        Set<DoctorSchedule> result = doctorScheduleRepository.findByDoctor_Id(doctor.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByDoctor_Id: no retorna horarios de otros doctores")
    void findByDoctorId_NotReturnOtherDoctorsSchedules() {

        // solo doctor2 tiene horarios
        saveSchedule(doctor2, DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0));
        saveSchedule(doctor2, DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(18, 0));

        // buscar horarios de doctor (no doctor2)
        Set<DoctorSchedule> result = doctorScheduleRepository.findByDoctor_Id(doctor.getId());

        assertThat(result).isEmpty();
    }
}