package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findById(Long id);


    //Patients with the highest amount of NO_SHOW between dates
    @Query("SELECT p, COUNT(a) as noShows FROM Patient p JOIN p.appointments a " +
            "WHERE a.status = AppointmentStatus.NO_SHOW " +
            "AND a.startAt BETWEEN :lowLimit AND :highLimit " +
            "GROUP BY p ORDER BY noShows DESC")
    List<Object[]> highestNoShowPatientsBetweenDates(
            @Param("lowLimit") LocalDateTime lowLimit,
            @Param("highLimit") LocalDateTime highLimit
    );
}
