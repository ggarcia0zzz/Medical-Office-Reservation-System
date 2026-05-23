package com.example.medicalofficereservationsystem.security.controller;

import com.example.medicalofficereservationsystem.security.Repository.AppUserRepository;
import com.example.medicalofficereservationsystem.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository repo;
    private final BCryptPasswordEncoder enconder;
    private final AuthenticationManager authManager;
    private final JwtService service;


}
