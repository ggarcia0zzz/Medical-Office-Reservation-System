package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.entities.AppointmentType;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import com.example.medicalofficereservationsystem.repository.AppointmentRepository;
import com.example.medicalofficereservationsystem.repository.AppointmentTypeRepository;
import com.example.medicalofficereservationsystem.repository.DoctorScheduleRepository;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AvailabilitySlotServiceImpl implements AvailabilitySlotService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<AvailabilitySLotResponse> getAvailabilitySlotsFromDoctor(Long doctorId, LocalDate date, Long appointmentTypeId) {

        AppointmentType appointmentType = appointmentTypeRepository.findById(appointmentTypeId).orElseThrow();

        int duration  = appointmentType.getDurationMinutes();

        List<Appointment>  appointments = appointmentRepository.findByDoctorAndDate(doctorId, date);

        List<AvailabilitySLotResponse> slots = new ArrayList<>();

        DoctorSchedule schedule = doctorScheduleRepository.findByDayOfWeekAndDoctor_Id(date.getDayOfWeek(), doctorId).orElseThrow();

        LocalTime auxStart = schedule.getStartTime();

        LocalTime auxEnd = auxStart.plusMinutes(duration);

        while(auxEnd.isBefore(schedule.getEndTime()) || auxEnd.equals(schedule.getEndTime())) {

            LocalTime slotStart = auxStart;
            LocalTime slotEnd = auxEnd;

            boolean isFree = appointments.stream().noneMatch(a -> slotStart.isBefore(a.getEndAt().toLocalTime()) && slotEnd.isAfter(a.getStartAt().toLocalTime()));

            if(isFree) {
                slots.add(doctorScheduleMapper.toSlotResponse(slotStart, slotEnd));
            }

            auxStart = auxEnd;
            auxEnd = auxStart.plusMinutes(duration);

        }

        return slots;
    }

}
