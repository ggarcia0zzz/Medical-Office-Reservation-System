package com.example.medicalofficereservationsystem;

import org.springframework.boot.SpringApplication;

public class TestMedicalOfficeReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(MedicalOfficeReservationSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
