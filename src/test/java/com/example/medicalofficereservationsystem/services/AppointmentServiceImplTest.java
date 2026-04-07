package com.example.medicalofficereservationsystem.services;

import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos.*;
import com.example.medicalofficereservationsystem.entities.Appointment;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import com.example.medicalofficereservationsystem.repository.AppointmentRepository;
import com.example.medicalofficereservationsystem.service.AppointmentServiceImpl;
import com.example.medicalofficereservationsystem.service.Mapper.AppointmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private final LocalDateTime future_time = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

    private Appointment appointment;
    private AppointmentResponse response;

    @BeforeEach
    void setUp() {
        appointment = Appointment.builder().id(1L).startAt(future_time).endAt(future_time.plusMinutes(30)).status(AppointmentStatus.SCHEDULED)
                .createdAt(Instant.now())
                .build();

        response = new AppointmentResponse(1L, future_time, future_time.plusMinutes(30), AppointmentStatus.SCHEDULED,
                1L, 1L, 1L, 1L);
    }


    @Test
    @DisplayName("createAppointment: crea y retorna la cita correctamente")
    void createAppointment_CreateAndReturnResponse() {
        var req = new AppointmentCreateRequest(future_time, future_time.plusMinutes(30),
                1L, 1L, 1L, 1L);

        when(appointmentMapper.toEntity(req)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(response);

        AppointmentResponse result = appointmentService.createAppointment(req);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.status()).isEqualTo(AppointmentStatus.SCHEDULED);

        verify(appointmentMapper).toEntity(req);
        verify(appointmentRepository).save(appointment);
        verify(appointmentMapper).toResponse(appointment);
    }

    @Test
    @DisplayName("getAppointmentById: retorna la cita cuando existe")
    void getAppointmentById_ReturnResponseWhenExists() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toResponse(appointment)).thenReturn(response);

        AppointmentResponse result = appointmentService.getAppointmentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getAppointmentById: lanza excepción cuando la cita no existe")
    void getAppointmentById_ThrowExceptionWhenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.getAppointmentById(99L)).isInstanceOf(java.util.NoSuchElementException.class);
    }


    @Test
    @DisplayName("confirmAppointment: cambia el estado a CONFIRMED correctamente")
    void confirmAppointment_SetStatusConfirmed() {
        AppointmentResponse confirmedResponse = new AppointmentResponse(1L, future_time, future_time.plusMinutes(30),
                AppointmentStatus.CONFIRMED, 1L, 1L, 1L, 1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(confirmedResponse);

        AppointmentResponse result = appointmentService.confirmAppointment(1L);

        assertThat(result.status()).isEqualTo(AppointmentStatus.CONFIRMED);
        verify(appointmentRepository).save(appointment);
        // verificar que se hizo el set del estado directamente en la entidad
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    @DisplayName("confirmAppointment: lanza excepción cuando la cita no existe")
    void confirmAppointment_shouldThrow_whenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.confirmAppointment(99L))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }


    @Test
    @DisplayName("noShowAppointment: cambia el estado a NO_SHOW correctamente")
    void noShowAppointment_shouldSetStatusNoShow() {
        AppointmentResponse noShowResponse = new AppointmentResponse(1L, future_time, future_time.plusMinutes(30),
                AppointmentStatus.NO_SHOW, 1L, 1L, 1L, 1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(noShowResponse);

        AppointmentResponse result = appointmentService.noShowAppointment(1L);

        assertThat(result.status()).isEqualTo(AppointmentStatus.NO_SHOW);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.NO_SHOW);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("noShowAppointment: lanza excepción cuando la cita no existe")
    void noShowAppointment_shouldThrow_whenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.noShowAppointment(99L)).isInstanceOf(java.util.NoSuchElementException.class);
    }


    @Test
    @DisplayName("completeAppointment: completa la cita y aplica observaciones del mapper")
    void completeAppointment_shouldCompleteAndApplyObservations() {
        AppointmentResponse completedResponse = new AppointmentResponse(1L, future_time, future_time.plusMinutes(30),
                AppointmentStatus.COMPLETED, 1L, 1L, 1L, 1L);

        var comRequest = new AppointmentCompletionRequest(1L, "Paciente estable");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(completedResponse);

        AppointmentResponse result = appointmentService.completeAppointment(1L, comRequest);

        assertThat(result.status()).isEqualTo(AppointmentStatus.COMPLETED);
        // verificar que el mapper aplicó los datos de completion sobre la entidad
        verify(appointmentMapper).completionFromRequest(appointment, comRequest);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("completeAppointment: lanza excepción cuando la cita no existe")
    void completeAppointment_shouldThrow_whenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.completeAppointment(99L,
                new AppointmentCompletionRequest(99L, "obs")))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }


    @Test
    @DisplayName("cancelAppointment: cancela la cita y aplica el motivo del mapper")
    void cancelAppointment_shouldCancelAndApplyReason() {
        AppointmentResponse cancelledResponse = new AppointmentResponse(1L, future_time, future_time.plusMinutes(30),
                AppointmentStatus.CANCELLED, 1L, 1L, 1L, 1L);

        var cancelRequest = new AppointmentCancelRequest(1L, "Paciente no puede asistir");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(appointmentMapper.toResponse(appointment)).thenReturn(cancelledResponse);

        AppointmentResponse result = appointmentService.cancelAppointment(1L, cancelRequest);

        assertThat(result.status()).isEqualTo(AppointmentStatus.CANCELLED);
        // verificar que el mapper aplicó los datos de cancelación sobre la entidad
        verify(appointmentMapper).cancelFromRequest(appointment, cancelRequest);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("cancelAppointment: lanza excepción cuando la cita no existe")
    void cancelAppointment_shouldThrow_whenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.cancelAppointment(99L, new AppointmentCancelRequest(99L, "motivo")))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }
}