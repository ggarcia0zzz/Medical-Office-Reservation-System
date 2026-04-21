package com.example.medicalofficereservationsystem.controllers;

import com.example.medicalofficereservationsystem.Exceptions.NotFoundException;
import com.example.medicalofficereservationsystem.api.dto.DoctorDtos;
import com.example.medicalofficereservationsystem.api.dto.api.GlobalExceptionHandler;
import com.example.medicalofficereservationsystem.api.dto.controller.DoctorController;
import com.example.medicalofficereservationsystem.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.Set;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class DoctorControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @BeforeEach
    void setUp(){

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).setControllerAdvice(new GlobalExceptionHandler()).build();

    }

    @Test
    @DisplayName("201 cuando el doctor se crea correctamente")
    void createDoctorReturns201WhenValid() throws Exception{

        var req = new DoctorDtos.DoctorCreateRequest("Dr Garcia","garcia@gmail.com","LIC-1234");
        var resp = new DoctorDtos.DoctorResponse(1L,"Dr Garcia","garcia@gmail.com","LIC-1234",null, Set.of(),Set.of());
        when(doctorService.createDoctor(any())).thenReturn(resp);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Dr Garcia"))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("400 cuando el nombre está vacío")
    void createDoctorReturns400WhenFullNameIsBlank() throws Exception {
        String invalidJson = """
                {
                  "fullName": "",
                  "email": "garcia@gmail.com",
                  "licenseNumber": "LIC-1234"
                }
                """;

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }

    @Test
    @DisplayName("400 cuando el email tiene formato inválido")
    void createDoctorReturns400WhenEmailIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "fullName": "Dr Garcia",
                  "email": "not-an-email",
                  "licenseNumber": "LIC-1234"
                }
                """;

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].field").value("email"));
    }

    @Test
    @DisplayName("400 cuando el número de licencia tiene formato inválido")
    void createDoctorReturns400WhenLicenseNumberFormatIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "fullName": "Dr Garcia",
                  "email": "garcia@gmail.com",
                  "licenseNumber": "INVALID"
                }
                """;

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].field").value("licenseNumber"));
    }

    @Test
    @DisplayName("200 cuando el doctor existe")
    void getDoctorReturns200WhenFound() throws Exception {
        var resp = new DoctorDtos.DoctorResponse(1L, "Dr Garcia", "garcia@gmail.com", "LIC-1234",
                null, Set.of(), Set.of());

        when(doctorService.getDoctorById(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Dr Garcia"));
    }

    @Test
    @DisplayName("404 cuando el doctor no existe")
    void getDoctorReturns404WhenNotFound() throws Exception {
        when(doctorService.getDoctorById(99L))
                .thenThrow(new NotFoundException("Doctor con el id 99 no encontrado"));

        mockMvc.perform(get("/api/doctors/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor con el id 99 no encontrado"));
    }

    @Test
    @DisplayName("200 con lista de doctores")
    void getAllDoctorsReturns200WithList() throws Exception {
        var d1 = new DoctorDtos.DoctorResponse(1L, "Dr Garcia", "garcia@gmail.com", "LIC-1234", null, Set.of(), Set.of());
        var d2 = new DoctorDtos.DoctorResponse(2L, "Dr Orozco", "orozco@gmail.com", "LIC-5678", null, Set.of(), Set.of());

        when(doctorService.getAllDoctors()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName").value("Dr Garcia"))
                .andExpect(jsonPath("$[1].fullName").value("Dr Orozco"));
    }

    @Test
    @DisplayName("200 con lista vacía cuando no hay doctores")
    void getAllDoctorsReturns200WithEmptyList() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(List.of());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("200 cuando la actualización es exitosa")
    void updateDoctor_returns200_whenValid() throws Exception {
        var req = new DoctorDtos.DoctorUpdateRequest("Dr Garcia Actualizado", "garcia@gmail.com", "LIC-1234", 1L);
        var resp = new DoctorDtos.DoctorResponse(1L, "Dr Garcia Actualizado", "garcia2@gmail.com", "LIC-1234",
                null, Set.of(), Set.of());

        when(doctorService.updateDoctor(eq(1L), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Dr Garcia Actualizado"));

    }

    @Test
    @DisplayName("404 cuando el doctor a actualizar no existe")
    void updateDoctor_returns404_whenNotFound() throws Exception {
        var req = new DoctorDtos.DoctorUpdateRequest("Dr Garcia", "garcia@gmail.com", "LIC-1234", 1L);

        when(doctorService.updateDoctor(eq(99L), any()))
                .thenThrow(new NotFoundException("Doctor con el id 99 no encontrado"));

        mockMvc.perform(patch("/api/doctors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor con el id 99 no encontrado"));
    }

}


