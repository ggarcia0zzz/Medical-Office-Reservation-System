package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.Patient;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //Find appointment from a patient by status
    Set<Appointment> findByStatusAndPatient(AppointmentStatus status, Patient patient);

    //Find Appointments by date range
    Set<Appointment> findByStartAtBetween(LocalDateTime startAtAfter, LocalDateTime startAtBefore);

    //Find Appointment by id
    Optional<Appointment> findById(Long Id);

    //Check for overlapping. This method returns true if there are any clash in the appointments of a doctor
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.startAt < :endAt AND a.endAt > :startAt")
    boolean existsOverlappingAppointmentByDoctor(@Param("doctorId") Long doctorId,
                                         @Param("startAt") LocalDateTime startAt,
                                         @Param("endAt") LocalDateTime endAt
    );

    //Check for overlapping. This method returns true if there are any clash in the appointments of an office
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.office.id = :officeId " +
            "AND a.startAt < :endAt AND a.endAt > :startAt")
    boolean existsOverlappingAppointmentByOffice(@Param("officeId") Long officeId,
                                         @Param("startAt") LocalDateTime startAt,
                                         @Param("endAt") LocalDateTime endAt
    );

    //Amount of appointments that were cancelled or the patient didnt show up by doctor's speciality
    @Query("SELECT COUNT(a) FROM Appointment a JOIN a.doctor d " +
            "WHERE (a.status = AppointmentStatus.CANCELLED OR a.status = AppointmentStatus.NO_SHOW) AND d.specialty.id = :specialtyId")
    Long amountOfCanceledOrNoShowAppointmentBySpecialty(@Param("specialtyId") Long specialtyId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND " +
            "CAST(a.startAt as localdate) = :date AND a.status != CANCELLED")
    List<Appointment> findByDoctorAndDate(@Param("doctorId") Long doctorId,
                                          @Param("date") LocalDate date);


    //Count number of appointments with an specific status of a specific doctor
    Long countAppointmentByStatusIsAndDoctor_IdIs(AppointmentStatus status, Long doctorId);

    //Count total number of appointments of a specific doctor
    Long countAppointmentByDoctor_IdIs(Long doctorId);

    //Get number of noShows of all patients from most to least
    @Query("SELECT p, COUNT(a) as noShows FROM Patient p JOIN p.appointments a " +
            "WHERE a.status = AppointmentStatus.NO_SHOW " +
            "GROUP BY p ORDER BY noShows DESC")
    List<Object[]> findPatientsByNoShows(Pageable pageable);

}
