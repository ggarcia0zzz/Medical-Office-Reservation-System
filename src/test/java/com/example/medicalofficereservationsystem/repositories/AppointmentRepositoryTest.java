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
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AppointmentRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    AppointmentTypeRepository appointmentTypeRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    private final LocalDateTime BASE_TIME = LocalDateTime.of(2025, 1, 15, 10, 0);

    private Doctor doctor;
    private Office office;
    private Patient patientOne;
    private Patient patientTwo;
    private AppointmentType appointmentType;


    @BeforeEach
    void setUp(){

        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();;
        officeRepository.deleteAll();
        appointmentRepository.deleteAll();;
        appointmentTypeRepository.deleteAll();;
        specialtyRepository.deleteAll();

        Specialty specialty= specialtyRepository.save(Specialty.builder().name("Cardiology").build());
        doctor = doctorRepository.save(Doctor.builder().fullName("Dr Lopez").email("lopez@gmail.com").licenseNumber("LIC-111").active(true).specialty(specialty).createdAt(Instant.now()).build());
        patientOne = patientRepository.save(Patient.builder().fullName("Juan Garcia").phone("3054715961").email("jgarcia@gmail.com").status(PatientStatus.ACTIVE).createdAt(Instant.now()).build());
        patientTwo = patientRepository.save(Patient.builder().fullName("Saray Cantillo").phone("3007333348").email("sarita@gmail.com").status(PatientStatus.ACTIVE).createdAt(Instant.now()).build());
        office = officeRepository.save(Office.builder().name("Consultorio 2").location("Piso 2").openingHour(LocalTime.of(8, 0)).closingHour(LocalTime.of(18, 0)).status(OfficeStatus.AVAILABLE).createdAt(Instant.now()).build());
        appointmentType = appointmentTypeRepository.save(AppointmentType.builder().name("Medicina General").durationMinutes(60).build());


    }

    private Appointment saveAppointment(Patient pa, Doctor doc, Office off, LocalDateTime start, LocalDateTime end, AppointmentStatus status) {
        return appointmentRepository.save(Appointment.builder()
                .patient(pa)
                .doctor(doc)
                .office(off)
                .appointmentType(appointmentType)
                .startAt(start)
                .endAt(end)
                .createdAt(Instant.now())
                .status(status)
                .build());
    }



    @Test
    @DisplayName("findByStatusAndPatient : Encontrar por paciente y estado")
    void findByStatusAndPatient(){

        saveAppointment(patientOne,doctor,office,
                BASE_TIME,
                BASE_TIME.plusMinutes(30),
                AppointmentStatus.SCHEDULED);
        saveAppointment(patientOne,doctor,office,
                BASE_TIME.plusHours(1),
                BASE_TIME.plusMinutes(90),
                AppointmentStatus.CANCELLED);
        saveAppointment(patientTwo,doctor,office,
                BASE_TIME.plusHours(2),
                BASE_TIME.plusMinutes(150),
                AppointmentStatus.SCHEDULED);

        Set<Appointment> result = appointmentRepository.findByStatusAndPatient(AppointmentStatus.SCHEDULED,patientOne);

        assertThat(result).hasSize(1);

        Appointment found = result.iterator().next();
        assertThat(found.getPatient().getId()).isEqualTo(patientOne.getId());
        assertThat(found.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);

    }

    @Test
    @DisplayName("findByStartAtBetween: encuentra citas entre el rango")
    void findByStartAtBetween(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusMinutes(30),
                AppointmentStatus.SCHEDULED);

        saveAppointment(patientTwo,doctor,office,BASE_TIME.plusHours(2),
                BASE_TIME.plusHours(3),AppointmentStatus.CONFIRMED);

        saveAppointment(patientOne,doctor,office,BASE_TIME.minusDays(1),
                BASE_TIME.minusDays(1).plusMinutes(30),AppointmentStatus.SCHEDULED);

        saveAppointment(patientTwo,doctor,office,BASE_TIME.plusDays(1),
                BASE_TIME.plusDays(1).plusMinutes(30),
                AppointmentStatus.SCHEDULED);

        Set<Appointment> result = appointmentRepository.findByStartAtBetween(BASE_TIME.toLocalDate().atStartOfDay(),
                BASE_TIME.toLocalDate().atTime(23,59,59));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a-> !a.getStartAt().toLocalDate().isBefore(BASE_TIME.toLocalDate())&& !a.getStartAt().toLocalDate().isAfter(BASE_TIME.toLocalDate()) );
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByDoctor: cuando SI hay traslape")
    void existsOverlappingAppointmentByDoctor_WhenOverlapExists(){

        saveAppointment(patientOne,doctor,office,
                BASE_TIME, BASE_TIME.plusHours(1),
                AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByDoctor(doctor.getId(),BASE_TIME.plusMinutes(30),
                BASE_TIME.plusMinutes(90));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByDoctor: cuando NO hay traslape")
    void existsOverlappingAppointmentByDoctor_WhenOverlapDoesNotExists(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByDoctor(doctor.getId(),
                BASE_TIME.plusMinutes(90),BASE_TIME.plusMinutes(150));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByDoctor: false cuando los slots son adyacentes")
    void existsOverlappingAppointmentByDoctor_falseWhenSlotsAreAdjacent(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,BASE_TIME.plusHours(1),
                AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByDoctor(doctor.getId(),BASE_TIME.plusHours(1),
                BASE_TIME.plusHours(2));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByDoctor: no afectas a otros doctores con el mismo horario")
    void existsOverlappingAppointmentByDoctor_ShouldNotAffectOtherDoctors(){

        Doctor doctor2 = doctorRepository.save(Doctor.builder().fullName("Dr Orozco").email("orozco2@gmail.com")
                .licenseNumber("LIC-333").active(true).specialty(doctor.getSpecialty()).createdAt(Instant.now()).build());

        saveAppointment(patientOne,doctor2,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByDoctor(doctor.getId(),BASE_TIME.plusMinutes(30),
                BASE_TIME.plusMinutes(90));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByOffice: true cuando SI hay traslape en el consultorio")
    void existsOverlappingAppointmentByOffice_WhenOverlapExists(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByOffice(office.getId(),BASE_TIME.plusMinutes(30),
                BASE_TIME.plusMinutes(90));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByOffice: false cuando NO hay traslape")
    void existsOverlappingAppointmentByOffice_WhenOverlapDoesNotExists(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByOffice(office.getId(),BASE_TIME.plusMinutes(90),
                BASE_TIME.plusMinutes(150));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByOffice: false cuando slots son adyacentes")
    void existsOverlappingAppointmentByOffice_falseWhenSlotsAreAdjacent(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByOffice(office.getId(),BASE_TIME.plusHours(1),
                BASE_TIME.plusHours(2));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsOverlappingAppointmentByOffice: no afecta a otros consultorios que tengan el mismo horario")
    void existsOverlappingAppointmentByOffice_shouldNotAffectOtherOffices(){

        Office office2 = officeRepository.save(Office.builder().name("Consultorio 1").location("Piso 1").status(OfficeStatus.AVAILABLE)
                .createdAt(Instant.now()).build());

        saveAppointment(patientOne,doctor,office2,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.CONFIRMED);

        boolean result = appointmentRepository.existsOverlappingAppointmentByOffice(office.getId(),BASE_TIME.plusMinutes(30),
                BASE_TIME.plusMinutes(90));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("mountOfCanceledOrNoShowAppointmentBySpecialty: cuenta cancelled y no_show")
    void amountOfCanceledOrNoShowAppointmentBySpecialty(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,
                BASE_TIME.plusHours(1),AppointmentStatus.NO_SHOW);

        saveAppointment(patientTwo,doctor,office,BASE_TIME.plusHours(1),
                BASE_TIME.plusHours(2),AppointmentStatus.NO_SHOW);

        saveAppointment(patientOne, doctor, office, BASE_TIME.plusHours(2),
                BASE_TIME.plusHours(3),
                AppointmentStatus.CANCELLED);
        saveAppointment(patientTwo, doctor, office,BASE_TIME.plusHours(3),
                BASE_TIME.plusHours(4),
                AppointmentStatus.CONFIRMED);

        Long result = appointmentRepository.amountOfCanceledOrNoShowAppointmentBySpecialty(doctor.getSpecialty().getId());

        assertThat(result).isEqualTo(3L);
    }

    @Test
    @DisplayName("amountOfCanceledOrNoShowAppointmentBySpecialty: devuelve 0 solo cuando hay confirmed")
    void amountOfCanceledOrNoShowAppointmentBySpecialty_returZeroWhenOnlyConfirmed(){

        saveAppointment(patientOne,doctor,office,BASE_TIME,BASE_TIME.plusHours(1),
                AppointmentStatus.CONFIRMED);

        saveAppointment(patientTwo, doctor, office, BASE_TIME.plusHours(1),
                BASE_TIME.plusHours(2),
                AppointmentStatus.CONFIRMED);

        Long result = appointmentRepository.amountOfCanceledOrNoShowAppointmentBySpecialty(doctor.getSpecialty().getId());

        assertThat(result).isZero();
    }

    @Test
    @DisplayName("amountOfCanceledOrNoShowAppointmentBySpecialty: no cuenta otras especialidades")
    void amountOfCanceledOrNoShowAppointmentBySpecialty_DoesNotCountOtherSpecialties(){

        Specialty otherSpecialty = specialtyRepository.save(Specialty.builder().name("Neurología").build());

        Doctor otherDoctor = doctorRepository.save(Doctor.builder().fullName("Dr. Good").email("good@gmail.com")
                .licenseNumber("LIC-222").active(true).specialty(otherSpecialty)
                .createdAt(Instant.now())
                .build());

        saveAppointment(patientOne, otherDoctor, office, BASE_TIME,
                BASE_TIME.plusHours(1),
                AppointmentStatus.NO_SHOW);

        saveAppointment(patientTwo, doctor, office, BASE_TIME.plusHours(1),
                BASE_TIME.plusHours(2),
                AppointmentStatus.NO_SHOW);

        Long result = appointmentRepository.amountOfCanceledOrNoShowAppointmentBySpecialty(doctor.getSpecialty().getId());

        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("amountOfCanceledOrNoShowAppointmentBySpecialty: retorna 0 cuando no hay citas")
    void amountOfCanceledOrNoShowAppointmentBySpecialty_returnZeroWhenNoAppointments(){

        Long result = appointmentRepository.amountOfCanceledOrNoShowAppointmentBySpecialty(doctor.getSpecialty().getId());

        assertThat(result).isZero();
    }
}