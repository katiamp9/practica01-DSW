package com.example.empleados.service;

import com.example.empleados.domain.CuentaEmpleado;
import com.example.empleados.repository.CuentaEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoDeleteServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private CuentaEmpleadoRepository cuentaEmpleadoRepository;

    @Test
    void shouldDeleteWhenEmpleadoExistsAndIsNotMainAdmin() {
        EmpleadoDeleteService service = new EmpleadoDeleteService(empleadoRepository, cuentaEmpleadoRepository);
        when(empleadoRepository.existsById("EMP-1002")).thenReturn(true);
        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1002")).thenReturn(Optional.empty());

        service.delete("EMP-1002");

        verify(empleadoRepository).deleteById("EMP-1002");
        verify(cuentaEmpleadoRepository, never()).deleteById(org.mockito.ArgumentMatchers.any());
        verify(cuentaEmpleadoRepository, never()).flush();
    }

    @Test
    void shouldDeleteLinkedCuentaBeforeDeletingEmpleado() {
        EmpleadoDeleteService service = new EmpleadoDeleteService(empleadoRepository, cuentaEmpleadoRepository);
        CuentaEmpleado cuenta = new CuentaEmpleado();
        UUID cuentaId = UUID.randomUUID();
        cuenta.setId(cuentaId);
        cuenta.setCorreo("empleado@empresa.com");

        when(empleadoRepository.existsById("EMP-1003")).thenReturn(true);
        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1003")).thenReturn(Optional.of(cuenta));

        service.delete("EMP-1003");

        InOrder inOrder = org.mockito.Mockito.inOrder(cuentaEmpleadoRepository, empleadoRepository);
        inOrder.verify(cuentaEmpleadoRepository).deleteById(cuentaId);
        inOrder.verify(cuentaEmpleadoRepository).flush();
        inOrder.verify(empleadoRepository).deleteById("EMP-1003");
    }

    @Test
    void shouldThrowForbiddenWhenTryingToDeleteMainAdmin() {
        EmpleadoDeleteService service = new EmpleadoDeleteService(empleadoRepository, cuentaEmpleadoRepository);
        CuentaEmpleado cuenta = new CuentaEmpleado();
        cuenta.setCorreo("admin@empresa.com");

        when(empleadoRepository.existsById("EMP-1001")).thenReturn(true);
        when(cuentaEmpleadoRepository.findByEmpleadoClave("EMP-1001")).thenReturn(Optional.of(cuenta));

        assertThrows(ForbiddenOperationException.class, () -> service.delete("EMP-1001"));
        verify(empleadoRepository, never()).deleteById("EMP-1001");
    }

    @Test
    void shouldThrowNotFoundWhenEmpleadoDoesNotExist() {
        EmpleadoDeleteService service = new EmpleadoDeleteService(empleadoRepository, cuentaEmpleadoRepository);
        when(empleadoRepository.existsById("EMP-9999")).thenReturn(false);

        assertThrows(EmpleadoNotFoundException.class, () -> service.delete("EMP-9999"));
        verify(empleadoRepository, never()).deleteById("EMP-9999");
    }
}