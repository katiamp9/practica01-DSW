package com.example.empleados.service;

import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.CredencialEmpleadoRepository;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CredencialEmpleadoServiceTest {

    @Mock
    private CuentaEmpleadoRepository cuentaEmpleadoRepository;

    @Mock
    private CredencialEmpleadoRepository credencialEmpleadoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CredencialEmpleadoService credencialEmpleadoService;

    @BeforeEach
    void setUp() {
        credencialEmpleadoService = new CredencialEmpleadoService(
            cuentaEmpleadoRepository,
            credencialEmpleadoRepository,
            empleadoRepository,
            passwordEncoder
        );
    }

    @Test
    void upsertAccessForEmpleado_shouldFailWhenPasswordWithoutEmail() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> credencialEmpleadoService.upsertAccessForEmpleado("EMP-1001", null, "admin123")
        );

        assertEquals("si envías password también debes enviar email", exception.getMessage());
    }

    @Test
    void createAccountWithCredential_shouldFailWhenOnlyEmailSent() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> credencialEmpleadoService.createAccountWithCredential("admin@empresa.com", "EMP-1001", null)
        );

        assertEquals("email y password deben enviarse juntos", exception.getMessage());
    }

    @Test
    void upsertAccessForEmpleado_shouldActivateAccountWhenMissingAndCredentialsAreValid() {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-1001");

        CuentaEmpleado cuentaGuardada = new CuentaEmpleado();
        cuentaGuardada.setId(UUID.randomUUID());
        cuentaGuardada.setCorreo("admin@empresa.com");
        cuentaGuardada.setEmpleado(empleado);

        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1001")).thenReturn(Optional.empty());
        when(cuentaEmpleadoRepository.findByCorreoIgnoreCase("admin@empresa.com")).thenReturn(Optional.empty());
        when(cuentaEmpleadoRepository.existsByCorreoIgnoreCase("admin@empresa.com")).thenReturn(false);
        when(empleadoRepository.findById("EMP-1001")).thenReturn(Optional.of(empleado));
        when(cuentaEmpleadoRepository.save(any(CuentaEmpleado.class))).thenReturn(cuentaGuardada);
        when(passwordEncoder.encode("admin123")).thenReturn("$2y$10$hash");

        credencialEmpleadoService.upsertAccessForEmpleado("EMP-1001", "admin@empresa.com", "admin123");

        verify(cuentaEmpleadoRepository).save(any(CuentaEmpleado.class));
        verify(credencialEmpleadoRepository).save(any(CredencialEmpleado.class));
    }

    @Test
    void upsertAccessForEmpleado_shouldUpdateEmailWithoutPasswordWhenAccountExists() {
        CuentaEmpleado cuenta = new CuentaEmpleado();
        UUID accountId = UUID.randomUUID();
        cuenta.setId(accountId);
        cuenta.setCorreo("old@empresa.com");

        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1001")).thenReturn(Optional.of(cuenta));
        when(cuentaEmpleadoRepository.findByCorreoIgnoreCase("nuevo@empresa.com")).thenReturn(Optional.empty());
        when(cuentaEmpleadoRepository.save(any(CuentaEmpleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        credencialEmpleadoService.upsertAccessForEmpleado("EMP-1001", "nuevo@empresa.com", null);

        assertEquals("nuevo@empresa.com", cuenta.getCorreo());
        verify(credencialEmpleadoRepository, never()).save(any(CredencialEmpleado.class));
    }

    @Test
    void upsertAccessForEmpleado_shouldFailWhenEmailDuplicatedInAnotherAccount() {
        CuentaEmpleado current = new CuentaEmpleado();
        current.setId(UUID.randomUUID());
        current.setCorreo("actual@empresa.com");

        CuentaEmpleado other = new CuentaEmpleado();
        other.setId(UUID.randomUUID());
        other.setCorreo("duplicado@empresa.com");

        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1001")).thenReturn(Optional.of(current));
        when(cuentaEmpleadoRepository.findByCorreoIgnoreCase(eq("duplicado@empresa.com"))).thenReturn(Optional.of(other));

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> credencialEmpleadoService.upsertAccessForEmpleado("EMP-1001", "duplicado@empresa.com", "admin123")
        );

        assertEquals("Ya existe una cuenta para el correo: duplicado@empresa.com", exception.getMessage());
    }
}
