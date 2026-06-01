package com.example.medicalofficereservationsystem.security.controller;

import com.example.medicalofficereservationsystem.entities.Doctor;
import com.example.medicalofficereservationsystem.entities.Patient;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.repository.PatientRepository;
import com.example.medicalofficereservationsystem.security.Repository.AppUserRepository;
import com.example.medicalofficereservationsystem.security.dtos.AuthDtos.*;
import com.example.medicalofficereservationsystem.security.entities.AppUser;
import com.example.medicalofficereservationsystem.security.enums.Role;
import com.example.medicalofficereservationsystem.security.jwt.JwtService;
import com.example.medicalofficereservationsystem.security.service.JpaUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository repo;
    private final BCryptPasswordEncoder enconder;
    private final AuthenticationManager authManager;
    private final JwtService service;
    private final JpaUserDetailsService  userDetailsService;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
        if(repo.existsByEmailIgnoreCase(req.email())){
            return ResponseEntity.badRequest().build();
        }
        var roles = Optional.ofNullable(req.roles()).filter(r -> !r.isEmpty())
                .orElseGet(() -> Set.of(Role.ROLE_PATIENT));

        var user = AppUser.builder()
                .email(req.email())
                .password(enconder.encode(req.password()))
                .roles(roles)
                .build();

        repo.save(user);

        if(roles.contains(Role.ROLE_PATIENT)){
            userDetailsService.linkUserToPatinet(req);
        } else if(roles.contains(Role.ROLE_DOCTOR)){
            userDetailsService.linkUserToDoctor(req);
        }

        var principal = User.withUsername(user.getEmail())
                .password(enconder.encode(req.password()))
                .authorities(roles.stream().map(Enum::name).toArray(String[]::new))
                .build();

        var token = service.generateToken(principal, Map.of("roles", roles));
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", service.getExpirationSeconds()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        var user = repo.findByEmailIgnoreCase(req.email()).orElseThrow();
        var principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
        var token = service.generateToken(principal, Map.of("roles", user.getRoles()));
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", service.getExpirationSeconds()));
    }

}
