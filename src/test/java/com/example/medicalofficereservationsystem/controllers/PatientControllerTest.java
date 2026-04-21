package com.example.medicalofficereservationsystem.controllers;

import com.example.medicalofficereservationsystem.Exceptions.NotFoundException;
import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.api.dto.api.GlobalExceptionHandler;
import com.example.medicalofficereservationsystem.api.dto.controller.PatientController;
import com.example.medicalofficereservationsystem.enums.PatientStatus;
import com.example.medicalofficereservationsystem.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.List;
import java.util.Set;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("201 cuando el paciente se crea correctamente")
    void createPatientReturns201WhenValid() throws Exception {
        var req = new PatientCreateRequest("Juan Cantillo", "juan@gmail.com", "3054715961");
        var resp = new PatientResponse(1L, "juan@gmail.com", "3054715961", PatientStatus.ACTIVE, Set.of());

        when(patientService.createPatient(any())).thenReturn(resp);
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("400 cuando el nombre está vacío")
    void createPatientReturns400WhenFullNameIsBlank() throws Exception {
        String invalidJson = """
                {
                  "fullName": "",
                  "email": "juan@gmail.com",
                  "phone": "3054715961"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].field").value("fullName"));
    }

    @Test
    @DisplayName("400 cuando el email tiene formato inválido")
    void createPatientReturns400WhenEmailIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "fullName": "Juan Cantillo",
                  "email": "no-es-email",
                  "phone": "3054715961"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].field").value("email"));
    }

    @Test
    @DisplayName("400 cuando el teléfono tiene formato inválido")
    void createPatientReturns400WhenPhoneIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "fullName": "Juan Cantillo",
                  "email": "juan@gmail.com",
                  "phone": "abc-invalid"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].field").value("phone"));
    }

    @Test
    @DisplayName("400 cuando múltiples campos son inválidos")
    void createPatientReturns400WhenMultipleFieldsInvalid() throws Exception {
        String invalidJson = """
                {
                  "fullName": "",
                  "email": "no-es-email",
                  "phone": ""
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray());
    }


    @Test
    @DisplayName("200 cuando el paciente existe")
    void getPatient_returns200_whenFound() throws Exception {
        var resp = new PatientResponse(1L, "juan@gmail.com", "3054715961", PatientStatus.ACTIVE, Set.of());

        when(patientService.getPatientById(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("juan@gmail.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("404 cuando el paciente no existe")
    void getPatientReturns404WhenNotFound() throws Exception {
        when(patientService.getPatientById(99L))
                .thenThrow(new NotFoundException("Paciente con el id 99 no encontrado"));

        mockMvc.perform(get("/api/patients/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente con el id 99 no encontrado"));
    }

    @Test
    @DisplayName("200 con lista de pacientes")
    void getAllPatientsReturns200WithList() throws Exception {
        var p1 = new PatientResponse(1L, "juan@gmail.com", "3054715961", PatientStatus.ACTIVE, Set.of());
        var p2 = new PatientResponse(2L, "saray@gmail.com", "3007333348", PatientStatus.ACTIVE, Set.of());

        when(patientService.getAllPatients()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("juan@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("saray@gmail.com"));
    }

    @Test
    @DisplayName("200 con lista vacía cuando no hay pacientes")
    void getAllPatientsReturns200WithEmptyList() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("200 cuando la actualización es exitosa")
    void updatePatientReturns200WhenValid() throws Exception {
        var req = new PatientUpdateRequest("Juan Cantillo Actualizado", "juan2@gmail.com", "3054715961",
                PatientStatus.ACTIVE, Set.of());
        var resp = new PatientResponse(1L, "juan2@gmail.com", "3054715961", PatientStatus.ACTIVE, Set.of());

        when(patientService.updatePatient(eq(1L), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan2@gmail.com"));
    }

    @Test
    @DisplayName("404 cuando el paciente a actualizar no existe")
    void updatePatientReturns404WhenNotFound() throws Exception {
        var req = new PatientUpdateRequest("Juan Cantillo", "juan@gmail.com", "3054715961",
                PatientStatus.ACTIVE, Set.of());

        when(patientService.updatePatient(eq(99L), any()))
                .thenThrow(new NotFoundException("Paciente con el id 99 no encontrado"));

        mockMvc.perform(patch("/api/patients/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente con el id 99 no encontrado"));
    }
}
