package com.example.medicalofficereservationsystem.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="specialties")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "specialty")
    private Set<Doctor> doctors;
}

