package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.enums.PatientDoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    //Find Doctor By id
    Optional<Doctor> findById(Long Id);

    List<Doctor> findAll();

    Set<Doctor> findBySpecialtyIdAndActive(Long specialtyId, PatientDoctorStatus active);

}
