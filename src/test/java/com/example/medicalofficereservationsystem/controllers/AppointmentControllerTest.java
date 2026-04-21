package com.example.medicalofficereservationsystem.controllers;

import com.example.medicalofficereservationsystem.Exceptions.BusinessException;
import com.example.medicalofficereservationsystem.Exceptions.ConflictException;
import com.example.medicalofficereservationsystem.Exceptions.NotFoundException;
import com.example.medicalofficereservationsystem.api.dto.AppointmentDtos;
import com.example.medicalofficereservationsystem.api.dto.api.GlobalExceptionHandler;
import com.example.medicalofficereservationsystem.api.dto.controller.AppointmentController;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import com.example.medicalofficereservationsystem.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.MediaType;


import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private static final LocalDateTime FUTURE_TIME = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    @DisplayName("201 cuando la cita se crea correctamente")
    void createAppointmentReturns201WhenValid() throws Exception{

        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME,null,1L,2L,3L,4L);
        var resp = new AppointmentDtos.AppointmentResponse(10L,FUTURE_TIME,FUTURE_TIME.plusMinutes(30), AppointmentStatus.SCHEDULED,1L,2L,3L,4L);

        when(appointmentService.createAppointment(any())).thenReturn(resp);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(header().exists("Location"));

    }

    @Test
    @DisplayName("400 cuando faltan campos obligatorios")
    void createAppointmentReturns400WhenMissingRequiredFields() throws Exception {

        String invalidJson = """
                {
                    "startAt": null,
                    "patientId": null,
                    "doctorId": null,
                    "OfficeId": null,
                    "AppointmentTypeId": null
                }
                """;

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }

    @Test
    @DisplayName("404 cuando el paciente no existe")
    void createAppointmentReturns404WhenPatientNotFound() throws Exception{

        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME,null,99L,2L,3L,4L);
        when(appointmentService.createAppointment(any()))
                .thenThrow(new NotFoundException("El paciente con id 99 no encontrado"));
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El paciente con id 99 no encontrado"));

    }

    @Test
    @DisplayName("409 cuando hay traslape de horario del doctor")
    void createAppointmentReturns409WhenDoctorScheduleOverlap() throws Exception {
        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME, null, 1L, 2L, 3L, 4L);

        when(appointmentService.createAppointment(any()))
                .thenThrow(new ConflictException("El doctor ya tiene una cita en ese rango de horario"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El doctor ya tiene una cita en ese rango de horario"));
    }

    @Test
    @DisplayName("409 cuando hay traslape de horario del consultorio")
    void createAppointmentReturns409WhenOfficeScheduleOverlap() throws Exception {
        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME, null, 1L, 2L, 3L, 4L);

        when(appointmentService.createAppointment(any()))
                .thenThrow(new ConflictException("El consultorio ya está ocupado en ese rango de horario"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El consultorio ya está ocupado en ese rango de horario"));
    }

    @Test
    @DisplayName("422 cuando el paciente está inactivo")
    void createAppointmentReturns422WhenPatientInactive() throws Exception {
        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME, null, 1L, 2L, 3L, 4L);

        when(appointmentService.createAppointment(any()))
                .thenThrow(new BusinessException("No se puede crear una cita para un paciente inactivo"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("No se puede crear una cita para un paciente inactivo"));
    }

    @Test
    @DisplayName("422 cuando la cita es en el pasado")
    void createAppointmentReturns422WhenDateIsInPast() throws Exception {
        var req = new AppointmentDtos.AppointmentCreateRequest(FUTURE_TIME, null, 1L, 2L, 3L, 4L);

        when(appointmentService.createAppointment(any()))
                .thenThrow(new BusinessException("No se puede crear una cita en una fecha y hora que ya pasó"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("No se puede crear una cita en una fecha y hora que ya pasó"));
    }

    @Test
    @DisplayName("200 cuando la confirmación es exitosa")
    void confirmAppointmentReturns200WhenValid() throws Exception {
        var resp = new AppointmentDtos.AppointmentResponse(1L, FUTURE_TIME, FUTURE_TIME.plusMinutes(30),
                AppointmentStatus.CONFIRMED, 1L, 2L, 3L, 4L);

        when(appointmentService.confirmAppointment(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/appointments/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("422 cuando la cita ya está cancelada")
    void confirmAppointmentReturns422WhenAlreadyCancelled() throws Exception {
        when(appointmentService.confirmAppointment(1L))
                .thenThrow(new BusinessException("Solo se puede confirmar una cita en estado SCHEDULED"));

        mockMvc.perform(patch("/api/appointments/1/confirm"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Solo se puede confirmar una cita en estado SCHEDULED"));
    }


    @Test
    @DisplayName("200 cuando la cancelación es exitosa")
    void cancelAppointmentReturns200WhenValid() throws Exception {
        var cancelReq = new AppointmentDtos.AppointmentCancelRequest(1L, "El paciente no puede asistir por viaje");
        var resp = new AppointmentDtos.AppointmentResponse(1L, FUTURE_TIME, FUTURE_TIME.plusMinutes(30),
                AppointmentStatus.CANCELLED, 1L, 2L, 3L, 4L);

        when(appointmentService.cancelAppointment(eq(1L), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/appointments/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("400 cuando el motivo de cancelación está vacío")
    void cancelAppointmentReturns400WhenReasonIsBlank() throws Exception {
        var cancelReq = new AppointmentDtos.AppointmentCancelRequest(1L, "");

        mockMvc.perform(patch("/api/appointments/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }

    @Test
    @DisplayName("404 cuando la cita no existe")
    void cancelAppointmentReturns404WhenNotFound() throws Exception {
        var cancelReq = new AppointmentDtos.AppointmentCancelRequest(99L, "Motivo válido de cancelación");

        when(appointmentService.cancelAppointment(eq(99L), any()))
                .thenThrow(new NotFoundException("Cita con el id 99 no encontrada"));

        mockMvc.perform(patch("/api/appointments/99/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cita con el id 99 no encontrada"));
    }

    @Test
    @DisplayName("200 cuando la finalización es exitosa")
    void completeAppointment_returns200_whenValid() throws Exception {
        var completeReq = new AppointmentDtos.AppointmentCompletionRequest(1L, "Paciente atendido sin complicaciones");
        var resp = new AppointmentDtos.AppointmentResponse(1L, FUTURE_TIME, FUTURE_TIME.plusMinutes(30),
                AppointmentStatus.COMPLETED, 1L, 2L, 3L, 4L);

        when(appointmentService.completeAppointment(eq(1L), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/appointments/1/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(completeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("200 cuando el no-show es exitoso")
    void noShowAppointment_returns200_whenValid() throws Exception {
        var resp = new AppointmentDtos.AppointmentResponse(1L, FUTURE_TIME, FUTURE_TIME.plusMinutes(30),
                AppointmentStatus.NO_SHOW, 1L, 2L, 3L, 4L);

        when(appointmentService.noShowAppointment(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/appointments/1/noshow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NO_SHOW"));
    }

}
