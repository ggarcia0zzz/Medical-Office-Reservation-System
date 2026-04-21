package com.example.medicalofficereservationsystem.controllers;

import com.example.medicalofficereservationsystem.Exceptions.BusinessException;
import com.example.medicalofficereservationsystem.Exceptions.NotFoundException;
import com.example.medicalofficereservationsystem.api.dto.DoctorScheduleDtos.AvailabilitySLotResponse;
import com.example.medicalofficereservationsystem.api.dto.api.GlobalExceptionHandler;
import com.example.medicalofficereservationsystem.api.dto.controller.AvailabilitySlotController;
import com.example.medicalofficereservationsystem.service.AvailabilitySlotService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilitySlotControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AvailabilitySlotService availabilitySlotService;

    @InjectMocks
    private AvailabilitySlotController availabilitySlotController;

    private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(3);

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(availabilitySlotController).setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("200 con slots disponibles")
    void getAvailabilitySlotsReturns200WithSlots() throws Exception {
        var slot1 = new AvailabilitySLotResponse(LocalTime.of(8, 0), LocalTime.of(9, 0));
        var slot2 = new AvailabilitySLotResponse(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var slot3 = new AvailabilitySLotResponse(LocalTime.of(11, 0), LocalTime.of(12, 0));

        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(1L), any(LocalDate.class), eq(2L)))
                .thenReturn(List.of(slot1, slot2, slot3));

        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].startTime").value("08:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("09:00:00"))
                .andExpect(jsonPath("$[1].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[2].startTime").value("11:00:00"));
    }

    @Test
    @DisplayName("200 con lista vacía cuando no hay slots")
    void getAvailabilitySlotsReturns200WhenNoSlotsAvailable() throws Exception {
        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(1L), any(LocalDate.class), eq(2L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("404 cuando el doctor no existe")
    void getAvailabilitySlotsReturns404WhenDoctorNotFound() throws Exception {
        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(99L), any(LocalDate.class), eq(2L)))
                .thenThrow(new NotFoundException("Doctor con el id 99 no encontrado"));

        mockMvc.perform(get("/api/availabilityslots/99")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor con el id 99 no encontrado"));
    }

    @Test
    @DisplayName("404 cuando el tipo de cita no existe")
    void getAvailabilitySlotsReturns404WhenAppointmentTypeNotFound() throws Exception {
        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(1L), any(LocalDate.class), eq(99L)))
                .thenThrow(new NotFoundException("Tipo de cita con el id 99 no encontrado"));

        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tipo de cita con el id 99 no encontrado"));
    }

    @Test
    @DisplayName("422 cuando el doctor no tiene horario configurado en ese día")
    void getAvailabilitySlotsReturns422WhenDoctorHasNoScheduleForDay() throws Exception {
        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(1L), any(LocalDate.class), eq(2L)))
                .thenThrow(new BusinessException("El doctor no tiene horario agendado para ese día de la semana"));

        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("El doctor no tiene horario agendado para ese día de la semana"));
    }

    @Test
    @DisplayName("400 cuando falta el parámetro date")
    void getAvailabilitySlotsReturns400WhenDateParamMissing() throws Exception {
        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("400 cuando falta el parámetro appointmentTypeId")
    void getAvailabilitySlotsReturns400WhenAppointmentTypeIdMissing() throws Exception {
        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("devuelve slots ordenados cronológicamente")
    void getAvailabilitySlotsReturnsSlotsInChronologicalOrder() throws Exception {
        var slot1 = new AvailabilitySLotResponse(LocalTime.of(8, 0), LocalTime.of(9, 0));
        var slot2 = new AvailabilitySLotResponse(LocalTime.of(10, 0), LocalTime.of(11, 0));
        var slot3 = new AvailabilitySLotResponse(LocalTime.of(14, 0), LocalTime.of(15, 0));

        when(availabilitySlotService.getAvailabilitySlotsFromDoctor(eq(1L), any(LocalDate.class), eq(2L)))
                .thenReturn(List.of(slot1, slot2, slot3));

        mockMvc.perform(get("/api/availabilityslots/1")
                        .param("date", FUTURE_DATE.toString())
                        .param("appointmentTypeId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value("08:00:00"))
                .andExpect(jsonPath("$[1].startTime").value("10:00:00"))
                .andExpect(jsonPath("$[2].startTime").value("14:00:00"));
    }
}
