package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos;



import java.time.LocalDate;
import java.util.List;

public interface AvailabilitySlotService {
    List<DoctorScheduleDtos.AvailabilitySLotResponse> getAvailabilitySlotsFromDoctor(Long doctorId,
                                                                                     LocalDate date, Long appointmentTypeId);


}
