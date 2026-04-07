package com.example.medicalofficereservationsystem.repositories;


import com.example.medicalofficereservationsystem.entities.*;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import com.example.medicalofficereservationsystem.enums.OfficeStatus;
import com.example.medicalofficereservationsystem.enums.PatientStatus;
import com.example.medicalofficereservationsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PatientRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    private final LocalDateTime BASE_TIME = LocalDateTime.of(2025, 1, 15, 10, 0);

    private Patient patientOne;
    private Patient patientTwo;
    private Doctor doctor;
    private Office office;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        officeRepository.deleteAll();
        specialtyRepository.deleteAll();

        Specialty specialty = specialtyRepository.save(Specialty.builder().name("Cardiología").build());

        doctor = doctorRepository.save(Doctor.builder().fullName("Dr. Lopez").email("lopez@gmail.com").licenseNumber("LIC-001")
                .active(true)
                .specialty(specialty)
                .createdAt(Instant.now())
                .build());

        office = officeRepository.save(Office.builder().name("Consultorio 1").location("Piso 1").
                openingHour(LocalTime.of(8, 0)).closingHour(LocalTime.of(18, 0)).
                status(OfficeStatus.AVAILABLE).createdAt(Instant.now()).build());

        patientOne = patientRepository.save(Patient.builder().fullName("Juan Garcia").email("jgarcia@gmail.com").phone("3054715961")
                .status(PatientStatus.ACTIVE)
                .createdAt(Instant.now())
                .build());

        patientTwo = patientRepository.save(Patient.builder().fullName("Saray Cantillo").email("sarita@gmail.com").phone("3007333348")
                .status(PatientStatus.ACTIVE)
                .createdAt(Instant.now())
                .build());
    }

    private void saveAppointment(Patient patient, LocalDateTime startAt, AppointmentStatus status) {
        appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .office(office)
                .startAt(startAt)
                .endAt(startAt.plusHours(1))
                .status(status)
                .createdAt(Instant.now())
                .build());
    }

    @Test
    @DisplayName("highestNoShowPatientsBetweenDates: retorna pacientes ordenados por NO_SHOW de mayor a menor")
    void highestNoShowPatientsBetweenDates_shouldReturnOrderedByNoShowDesc() {

        // patientOne: 3 no_show es decir debe aparecer primero
        saveAppointment(patientOne, BASE_TIME, AppointmentStatus.NO_SHOW);
        saveAppointment(patientOne, BASE_TIME.plusHours(1), AppointmentStatus.NO_SHOW);
        saveAppointment(patientOne, BASE_TIME.plusHours(2), AppointmentStatus.NO_SHOW);

        // patientTwo: 1 no_show es decir debe aparecer segundo
        saveAppointment(patientTwo, BASE_TIME.plusHours(3), AppointmentStatus.NO_SHOW);

        List<Object[]> result = patientRepository.highestNoShowPatientsBetweenDates(
                BASE_TIME.toLocalDate().atStartOfDay(),
                BASE_TIME.toLocalDate().atTime(23, 59, 59));

        assertThat(result).hasSize(2);

        // primer lugar: patientOne con 3 no_shows
        assertThat(((Patient) result.get(0)[0]).getId()).isEqualTo(patientOne.getId());
        assertThat((Long) result.get(0)[1]).isEqualTo(3L);

        // segundo lugar: patientTwo con 1 no_show
        assertThat(((Patient) result.get(1)[0]).getId()).isEqualTo(patientTwo.getId());
        assertThat((Long) result.get(1)[1]).isEqualTo(1L);
    }

    @Test
    @DisplayName("highestNoShowPatientsBetweenDates: ignora citas fuera del rango de fechas")
    void highestNoShowPatientsBetweenDates_IgnoreAppointmentsOutsideDateRange() {


        // dentro del rango: debe contar
        saveAppointment(patientOne, BASE_TIME, AppointmentStatus.NO_SHOW);

        // fuera del rango: día anterior es no debe contar
        saveAppointment(patientOne, BASE_TIME.minusDays(1), AppointmentStatus.NO_SHOW);

        // fuera del rango: día siguiente es decir no debe contar
        saveAppointment(patientOne, BASE_TIME.plusDays(1), AppointmentStatus.NO_SHOW);

        List<Object[]> result = patientRepository.highestNoShowPatientsBetweenDates(
                BASE_TIME.toLocalDate().atStartOfDay(),
                BASE_TIME.toLocalDate().atTime(23, 59, 59));

        assertThat(result).hasSize(1);
        assertThat((Long) result.get(0)[1]).isEqualTo(1L);
    }

    @Test
    @DisplayName("highestNoShowPatientsBetweenDates: ignora citas con estado diferente a NO_SHOW")
    void highestNoShowPatientsBetweenDates_IgnoreNonNoShowStatuses() {

        // misma fecha pero diferentes estados
        saveAppointment(patientOne, BASE_TIME, AppointmentStatus.CANCELLED);
        saveAppointment(patientOne, BASE_TIME.plusHours(1), AppointmentStatus.CONFIRMED);
        saveAppointment(patientOne, BASE_TIME.plusHours(2), AppointmentStatus.SCHEDULED);

        List<Object[]> result = patientRepository.highestNoShowPatientsBetweenDates(
                BASE_TIME.toLocalDate().atStartOfDay(),
                BASE_TIME.toLocalDate().atTime(23, 59, 59));

        // ningún paciente tiene no_sow: lista vacía
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("highestNoShowPatientsBetweenDates: retorna vacío cuando no hay citas en el rango")
    void highestNoShowPatientsBetweenDates_ReturnEmptyWhenNoAppointments() {

        // sin ninguna cita guardada
        List<Object[]> result = patientRepository.highestNoShowPatientsBetweenDates(
                BASE_TIME.toLocalDate().atStartOfDay(),
                BASE_TIME.toLocalDate().atTime(23, 59, 59));

        assertThat(result).isEmpty();
    }

}