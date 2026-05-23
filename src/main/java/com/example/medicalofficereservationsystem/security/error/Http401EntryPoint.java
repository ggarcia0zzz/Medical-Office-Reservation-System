package com.example.medicalofficereservationsystem.security.error;

import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

@Component
public class Http401EntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(),
                    Map.of("status", 401, "error", "Unauthorized", "message", "Authentication required"));
        }
        catch (IOException ignored) {}

    }
}
