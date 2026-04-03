package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Set;

public interface DoctorSchedule extends JpaRepository<DoctorSchedule,Long> {
    Set<DoctorSchedule> findByDayOfWeekAndDoctor(DayOfWeek dayOfWeek, Doctor doctor);

}
