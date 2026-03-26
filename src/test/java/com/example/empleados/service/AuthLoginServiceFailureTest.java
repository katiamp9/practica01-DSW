package com.example.empleados.service;

import com.example.empleados.controller.dto.AuthDtos;
import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLoginServiceFailureTest {

    @Mock
    private CuentaEmpleadoRepository cuentaEmpleadoRepository;

    @Mock
    private CredencialEmpleadoService credencialEmpleadoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationAuditService authenticationAuditService;

    private AuthLoginService authLoginService;

    @BeforeEach
    void setUp() {
        authLoginService = new AuthLoginService(
            cuentaEmpleadoRepository,
            credencialEmpleadoService,
            passwordEncoder,
            authenticationAuditService
        );
    }

    @Test
    void login_shouldReturnUniformErrorWhenEmailDoesNotExist() {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest(" NoExiste@empresa.com ", "admin123");

        when(cuentaEmpleadoRepository.findWithEmpleadoByCorreoIgnoreCase(eq("noexiste@empresa.com")))
            .thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
            InvalidCredentialsException.class,
            () -> authLoginService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(authenticationAuditService).logFailure("noexiste@empresa.com", "EMAIL_NOT_FOUND");
        verify(passwordEncoder, never()).matches(eq("admin123"), eq("$2y$10$hash"));
    }

    @Test
    void login_shouldReturnUniformErrorWhenPasswordIsIncorrect() {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest("ADMIN@empresa.com", "wrong-pass");

        UUID accountId = UUID.randomUUID();
        CuentaEmpleado cuentaEmpleado = new CuentaEmpleado();
        cuentaEmpleado.setId(accountId);
        cuentaEmpleado.setCorreo("admin@empresa.com");

        CredencialEmpleado credencialEmpleado = new CredencialEmpleado();
        credencialEmpleado.setPasswordHash("$2y$10$hash");

        when(cuentaEmpleadoRepository.findWithEmpleadoByCorreoIgnoreCase(eq("admin@empresa.com")))
            .thenReturn(Optional.of(cuentaEmpleado));
        when(credencialEmpleadoService.findCredentialByEmail(eq("admin@empresa.com")))
            .thenReturn(Optional.of(credencialEmpleado));
        when(passwordEncoder.matches(eq("wrong-pass"), eq("$2y$10$hash"))).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
            InvalidCredentialsException.class,
            () -> authLoginService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(authenticationAuditService).logFailure("admin@empresa.com", "INVALID_PASSWORD");
    }
}