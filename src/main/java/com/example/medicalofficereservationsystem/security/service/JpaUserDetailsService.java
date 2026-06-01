package com.example.medicalofficereservationsystem.security.service;

import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.Patient;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.repository.PatientRepository;
import com.example.medicalofficereservationsystem.security.Repository.AppUserRepository;
import com.example.medicalofficereservationsystem.security.dtos.AuthDtos.*;
import com.example.medicalofficereservationsystem.security.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final AppUserRepository repo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repo.findByEmailIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException(username));

        var authorities =  user.getRoles().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    public void linkUserToPatinet(RegisterRequest req){
            var patient = Patient.builder()
                    .fullName(req.fullName())
                    .email(req.email()).build();
            patientRepo.save(patient);
    }

    public void linkUserToDoctor(RegisterRequest req){
        var doctor = Doctor.builder()
                .fullName(req.fullName())
                .email(req.email())
                .licenseNumber(req.licenseNumber())
                .build();
        doctorRepo.save(doctor);
    }

}
