package com.example.empleados.controller;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.dto.AuthDtos;
import com.example.empleados.service.AuthLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
    AuthController.class,
    ApiExceptionHandler.class
})
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "SPRING_SECURITY_USER_NAME=admin",
    "SPRING_SECURITY_USER_PASSWORD_HASH=$2y$10$rDLkWjFUlUkWr9GIiK42OOLVqlFM1eBhAmDNxf4VTAQmV.p.JWO5i"
})
class AuthControllerSuccessIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthLoginService authLoginService;

    @Test
    void shouldLoginSuccessfullyAndReturnAuthenticatedTrue() throws Exception {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("admin@empresa.com", "admin123");
        when(authLoginService.login(any(AuthDtos.LoginRequest.class)))
            .thenReturn(new AuthDtos.LoginSuccessResponse(true, "ROLE_ADMIN", "Admin"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.authenticated").value(true))
            .andExpect(jsonPath("$.rol").value("ROLE_ADMIN"))
            .andExpect(jsonPath("$.nombre").value("Admin"));

        verify(authLoginService).login(any(AuthDtos.LoginRequest.class));
    }
}