package com.example.medicalofficereservationsystem.entities;

import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="appointment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "appointment")
    private Set<Appointment> appointments = new HashSet<>();
}
