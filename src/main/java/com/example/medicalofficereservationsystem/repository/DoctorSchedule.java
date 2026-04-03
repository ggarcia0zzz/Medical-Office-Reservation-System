package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

public interface DoctorSchedule extends JpaRepository<DoctorSchedule,Long> {
    Optional<DoctorSchedule> findByDayOfWeekAndDoctor(DayOfWeek dayOfWeek, Doctor doctor);

    Set<DoctorSchedule> findByDoctor_Id(Long doctorId);

}
