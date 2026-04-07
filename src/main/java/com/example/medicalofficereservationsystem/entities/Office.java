package com.example.medicalofficereservationsystem.entities;

import com.example.medicalofficereservationsystem.enums.OfficeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 100)
    private String location;

    @Column(name = "opening_hour",nullable = false)
    private LocalTime openingHour;

    @Column(name = "closing_hour",nullable = false)
    private LocalTime closingHour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OfficeStatus status = OfficeStatus.AVAILABLE;

    @OneToMany(mappedBy = "office")
    @Column(name = "appointments")
    @Builder.Default
    private Set<Appointment> appointments = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}
