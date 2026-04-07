package com.example.medicalofficereservationsystem.services;

import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos;
import com.example.medicalofficereservationsystem.entities.DoctorSchedule;
import com.example.medicalofficereservationsystem.repository.DoctorScheduleRepository;
import com.example.medicalofficereservationsystem.service.DoctorScheduleServiceImpl;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorScheduleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorScheduleServiceImplTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;
    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;

    @InjectMocks
    private DoctorScheduleServiceImpl doctorScheduleService;

    private DoctorSchedule schedule;
    private DoctorScheduleDtos.DoctorScheduleResponse response;

    @BeforeEach
    void setUp() {
        schedule = DoctorSchedule.builder()
                .id(1L)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .build();

        response = new DoctorScheduleDtos.DoctorScheduleResponse(
                1L, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), 1L);
    }



    @Test
    @DisplayName("createDoctorSchedule: crea y retorna el horario correctamente")
    void createDoctorSchedule_CreateAndReturnResponse() {
        var request = new DoctorScheduleDtos.DoctorScheduleCreateRequest(
                DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), 1L);

        when(doctorScheduleMapper.toEntity(request)).thenReturn(schedule);
        when(doctorScheduleRepository.save(schedule)).thenReturn(schedule);
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        DoctorScheduleDtos.DoctorScheduleResponse result =
                doctorScheduleService.createDoctorSchedule(request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.startTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(result.endTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(result.doctorId()).isEqualTo(1L);

        verify(doctorScheduleMapper).toEntity(request);
        verify(doctorScheduleRepository).save(schedule);
        verify(doctorScheduleMapper).toResponse(schedule);
    }

    @Test
    @DisplayName("createDoctorSchedule: guarda el horario exactamente una vez")
    void createDoctorSchedule_SaveOnlyOnce() {
        var request = new DoctorScheduleDtos.DoctorScheduleCreateRequest(
                DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), 1L);

        when(doctorScheduleMapper.toEntity(request)).thenReturn(schedule);
        when(doctorScheduleRepository.save(schedule)).thenReturn(schedule);
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        doctorScheduleService.createDoctorSchedule(request);

        verify(doctorScheduleRepository, times(1)).save(schedule);
    }

    @Test
    @DisplayName("createDoctorSchedule: retorna el response que produce el mapper")
    void createDoctorSchedule_ReturnMapperResponse() {
        var request = new DoctorScheduleDtos.DoctorScheduleCreateRequest(
                DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), 2L);

        DoctorSchedule fridaySchedule = DoctorSchedule.builder()
                .id(2L)
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(18, 0))
                .build();

        DoctorScheduleDtos.DoctorScheduleResponse fridayResponse =
                new DoctorScheduleDtos.DoctorScheduleResponse(
                        2L, DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), 2L);

        when(doctorScheduleMapper.toEntity(request)).thenReturn(fridaySchedule);
        when(doctorScheduleRepository.save(fridaySchedule)).thenReturn(fridaySchedule);
        when(doctorScheduleMapper.toResponse(fridaySchedule)).thenReturn(fridayResponse);

        DoctorScheduleDtos.DoctorScheduleResponse result =
                doctorScheduleService.createDoctorSchedule(request);

        assertThat(result.dayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        assertThat(result.startTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(result.endTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(result.doctorId()).isEqualTo(2L);
    }


    @Test
    @DisplayName("getDoctorScheduleByDoctorId: retorna el horario cuando existe")
    void getDoctorScheduleByDoctorId_ReturnResponseWhenExists() {
        when(doctorScheduleRepository.findByDoctor_Id(1L)).thenReturn(Set.of(schedule));
        when(doctorScheduleMapper.toResponse(schedule)).thenReturn(response);

        List<DoctorScheduleDtos.DoctorScheduleResponse> result =
                doctorScheduleService.getDoctorScheduleByDoctorId(1L);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).dayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.get(0).startTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(result.get(0).endTime()).isEqualTo(LocalTime.of(12, 0));

        verify(doctorScheduleRepository).findByDoctor_Id(1L);
        verify(doctorScheduleMapper).toResponse(schedule);
    }

    @Test
    @DisplayName("getDoctorScheduleByDoctorId: retorna lista vacía cuando no hay horarios")
    void getDoctorScheduleByDoctorId_ReturnEmptyWhenNoSchedules() {
        when(doctorScheduleRepository.findByDoctor_Id(99L)).thenReturn(Set.of());

        List<DoctorScheduleDtos.DoctorScheduleResponse> result =
                doctorScheduleService.getDoctorScheduleByDoctorId(99L);

        assertThat(result.isEmpty()).isTrue();
        verify(doctorScheduleMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("getDoctorScheduleByDoctorId: no llama al mapper cuando no hay horarios")
    void getDoctorScheduleByDoctorId_DoNotCallMapperWhenEmpty() {
        when(doctorScheduleRepository.findByDoctor_Id(99L)).thenReturn(Set.of());

        doctorScheduleService.getDoctorScheduleByDoctorId(99L);

        verify(doctorScheduleMapper, never()).toResponse(any());
    }
}