package com.example.medicalofficereservationsystem.api.dto;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class ReportDtos {

    //Equivalent to DoctorProductivityResponse
    public record DoctorProductivityReport(DoctorDtos.DoctorProductivityResponse doctorInfo,
                                           Long completedAppointments, Long totalAppointments,
                                           float productivityPercent) implements Serializable {};

}
