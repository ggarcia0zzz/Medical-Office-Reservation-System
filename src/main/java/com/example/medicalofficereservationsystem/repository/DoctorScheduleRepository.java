package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule,Long> {
    Optional<DoctorSchedule> findByDayOfWeekAndDoctor_Id(DayOfWeek dayOfWeek, Long doctorId);

    Set<DoctorSchedule> findByDoctor_Id(Long doctorId);

}
