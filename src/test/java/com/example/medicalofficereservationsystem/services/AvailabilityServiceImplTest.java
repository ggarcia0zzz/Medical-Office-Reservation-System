package com.example.medicalofficereservationsystem.services;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.entities.AppointmentType;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import com.example.medicalofficereservationsystem.repository.AppointmentRepository;
import com.example.medicalofficereservationsystem.repository.AppointmentTypeRepository;
import com.example.medicalofficereservationsystem.repository.DoctorScheduleRepository;
import com.example.medicalofficereservationsystem.service.AvailabilitySlotServiceImpl;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorScheduleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceImplTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;
    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;
    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AvailabilitySlotServiceImpl availabilitySlotService;

    private final LocalDate date = LocalDate.of(2025, 1, 20);
    private final Long doctorId  = 1L;
    private final Long appointmentTypeId = 1L;

    private AppointmentType appointmentType;
    private DoctorSchedule schedule;

    @BeforeEach
    void setUp() {
        appointmentType = AppointmentType.builder().id(1L).name("Consulta General").durationMinutes(30).build();

        schedule = DoctorSchedule.builder().id(1L).dayOfWeek(DayOfWeek.MONDAY).startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(10, 0))
                .build();
    }


    @Test
    @DisplayName("getAvailabilitySlots: retorna todos los slots cuando no hay citas")
    void getAvailabilitySlots_ReturnAllSlotsWhenNoAppointments() {
        when(appointmentTypeRepository.findById(appointmentTypeId))
                .thenReturn(Optional.of(appointmentType));
        when(appointmentRepository.findByDoctorAndDate(doctorId, date))
                .thenReturn(List.of());
        when(doctorScheduleRepository.findByDayOfWeekAndDoctor_Id(DayOfWeek.MONDAY, doctorId))
                .thenReturn(Optional.of(schedule));

        AvailabilitySLotResponse slot1 = new AvailabilitySLotResponse(LocalTime.of(8, 0), LocalTime.of(8, 30));
        AvailabilitySLotResponse slot2 = new AvailabilitySLotResponse(LocalTime.of(8, 30), LocalTime.of(9, 0));
        AvailabilitySLotResponse slot3 = new AvailabilitySLotResponse(LocalTime.of(9, 0), LocalTime.of(9, 30));
        AvailabilitySLotResponse slot4 = new AvailabilitySLotResponse(LocalTime.of(9, 30), LocalTime.of(10, 0));

        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(8, 0), LocalTime.of(8, 30))).thenReturn(slot1);
        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(8, 30), LocalTime.of(9, 0))).thenReturn(slot2);
        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(9, 0), LocalTime.of(9, 30))).thenReturn(slot3);
        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(9, 30), LocalTime.of(10, 0))).thenReturn(slot4);

        List<AvailabilitySLotResponse> result = availabilitySlotService
                .getAvailabilitySlotsFromDoctor(doctorId, date, appointmentTypeId);

        assertThat(result).hasSize(4);
        verify(doctorScheduleMapper, times(4)).toSlotResponse(any(), any());
    }

    @Test
    @DisplayName("getAvailabilitySlots: retorna slots libres excluyendo los ocupados")
    void getAvailabilitySlots_ExcludeOccupiedSlots() {

        Appointment existingAppointment = Appointment.builder()
                .startAt(LocalDateTime.of(2025, 1, 20, 8, 0))
                .endAt(LocalDateTime.of(2025, 1, 20, 8, 30))
                .build();

        when(appointmentTypeRepository.findById(appointmentTypeId))
                .thenReturn(Optional.of(appointmentType));
        when(appointmentRepository.findByDoctorAndDate(doctorId, date))
                .thenReturn(List.of(existingAppointment));
        when(doctorScheduleRepository.findByDayOfWeekAndDoctor_Id(DayOfWeek.MONDAY, doctorId))
                .thenReturn(Optional.of(schedule));

        AvailabilitySLotResponse slot2 = new AvailabilitySLotResponse(LocalTime.of(8, 30), LocalTime.of(9, 0));
        AvailabilitySLotResponse slot3 = new AvailabilitySLotResponse(LocalTime.of(9, 0), LocalTime.of(9, 30));
        AvailabilitySLotResponse slot4 = new AvailabilitySLotResponse(LocalTime.of(9, 30), LocalTime.of(10, 0));

        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(8, 30), LocalTime.of(9, 0))).thenReturn(slot2);
        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(9, 0), LocalTime.of(9, 30))).thenReturn(slot3);
        when(doctorScheduleMapper.toSlotResponse(LocalTime.of(9, 30), LocalTime.of(10, 0))).thenReturn(slot4);

        List<AvailabilitySLotResponse> result = availabilitySlotService
                .getAvailabilitySlotsFromDoctor(doctorId,date, appointmentTypeId);

        assertThat(result).hasSize(3);

        verify(doctorScheduleMapper, times(3)).toSlotResponse(any(), any());
    }

    @Test
    @DisplayName("getAvailabilitySlots: retorna lista vacía cuando todos los slots están ocupados")
    void getAvailabilitySlots_ReturnEmptyWhenAllSlotsOccupied() {

        List<Appointment> appointments = List.of(
                buildAppointment(8, 0, 8, 30),
                buildAppointment(8, 30, 9, 0),
                buildAppointment(9, 0, 9, 30),
                buildAppointment(9, 30, 10, 0)
        );

        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
        when(appointmentRepository.findByDoctorAndDate(doctorId, date)).thenReturn(appointments);
        when(doctorScheduleRepository.findByDayOfWeekAndDoctor_Id(DayOfWeek.MONDAY, doctorId)).thenReturn(Optional.of(schedule));

        List<AvailabilitySLotResponse> result = availabilitySlotService.getAvailabilitySlotsFromDoctor(doctorId, date, appointmentTypeId);

        assertThat(result).isEmpty();

        verify(doctorScheduleMapper, never()).toSlotResponse(any(), any());
    }


    @Test
    @DisplayName("getAvailabilitySlots: lanza excepción cuando el tipo de cita no existe")
    void getAvailabilitySlots_ThrowWhenAppointmentTypeNotFound() {
        when(appointmentTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> availabilitySlotService.getAvailabilitySlotsFromDoctor(doctorId, date, 99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("getAvailabilitySlots: lanza excepción cuando el doctor no tiene horario ese día")
    void getAvailabilitySlots_ThrowWhenDoctorHasNoSchedule() {
        when(appointmentTypeRepository.findById(appointmentTypeId))
                .thenReturn(Optional.of(appointmentType));

        doReturn(List.of()).when(appointmentRepository).findByDoctorAndDate(doctorId, date);

        when(doctorScheduleRepository.findByDayOfWeekAndDoctor_Id(DayOfWeek.MONDAY, doctorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> availabilitySlotService.getAvailabilitySlotsFromDoctor(doctorId, date, appointmentTypeId))
                .isInstanceOf(NoSuchElementException.class);
    }



    private Appointment buildAppointment(int startHour, int startMin, int endHour, int endMin) {
        return Appointment.builder().startAt(LocalDateTime.of(2025, 1, 20, startHour, startMin))
                .endAt(LocalDateTime.of(2025, 1, 20, endHour, endMin))
                .build();
    }
}