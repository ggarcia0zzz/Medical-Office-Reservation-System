package com.example.medicalofficereservationsystem.repository;

import com.example.medicalofficereservationsystem.entities.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office,Long> {

    Optional<Office> findByName(String name);

    Page<Office> findAll(Pageable pageable);

    @Query(value = "SELECT o.*," +
            "COALESCE(SUM(EXTRACT(EPOCH FROM (a.end_at - a.start_at))), 0) " +
            "FROM offices o LEFT JOIN appointments a ON a.office_id = o.id " +
            "AND a.start_at BETWEEN :startDate AND :endDate " +
            "GROUP BY o.id", nativeQuery = true)

    Page<Object[]> findOfficeOccupancyData(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

}
