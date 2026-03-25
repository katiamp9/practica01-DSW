package com.example.empleados.service;

import com.example.empleados.controller.dto.AuthDtos;
import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.domain.Empleado;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLoginServiceSuccessTest {

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
    void login_shouldAuthenticateWhenCredentialsAreValid() {
        AuthDtos.LoginRequest request = new AuthDtos.LoginRequest(" Admin@Empresa.com ", "admin123");

        UUID accountId = UUID.randomUUID();
        CuentaEmpleado cuentaEmpleado = new CuentaEmpleado();
        cuentaEmpleado.setId(accountId);
        cuentaEmpleado.setCorreo("admin@empresa.com");

        Empleado empleado = new Empleado();
        empleado.setRol("ROLE_ADMIN");
        empleado.setNombre("Administrador Inicial");
        cuentaEmpleado.setEmpleado(empleado);

        CredencialEmpleado credencialEmpleado = new CredencialEmpleado();
        credencialEmpleado.setPasswordHash("$2y$10$hash");

        when(cuentaEmpleadoRepository.findWithEmpleadoByCorreoIgnoreCase(eq("admin@empresa.com")))
            .thenReturn(Optional.of(cuentaEmpleado));
        when(credencialEmpleadoService.findCredentialByEmail(eq("admin@empresa.com")))
            .thenReturn(Optional.of(credencialEmpleado));
        when(passwordEncoder.matches(eq("admin123"), eq("$2y$10$hash"))).thenReturn(true);

        AuthDtos.LoginSuccessResponse response = authLoginService.login(request);

        assertTrue(response.authenticated());
        assertEquals("ROLE_ADMIN", response.rol());
        assertEquals("Administrador Inicial", response.nombre());
        verify(cuentaEmpleadoRepository).findWithEmpleadoByCorreoIgnoreCase("admin@empresa.com");
        verify(credencialEmpleadoService).findCredentialByEmail("admin@empresa.com");
        verify(passwordEncoder).matches("admin123", "$2y$10$hash");
        verify(authenticationAuditService).logSuccess("admin@empresa.com");
    }
}