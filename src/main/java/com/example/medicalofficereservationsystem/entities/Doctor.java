package com.example.medicalofficereservationsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name",length=150)
    private String fullName;

    @Column(nullable=false,unique=true,length=100)
    private String email;

    @Column(name="license_number",nullable = false,unique=true,length=50)
    private String licenseNumber;

    @Column(name="is_active",nullable=false)
    @Builder.Default
    private boolean active=true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name="specialty_id")
    private Specialty specialty;

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private Set<DoctorSchedule> doctorSchedules = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private Set<Appointment> appointments = new HashSet<>();

}

